package verrimar.coopcycle.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import verrimar.coopcycle.IntegrationTest;
import verrimar.coopcycle.domain.Commercant;
import verrimar.coopcycle.repository.CommercantRepository;
import verrimar.coopcycle.repository.search.CommercantSearchRepository;
import verrimar.coopcycle.service.dto.CommercantDTO;
import verrimar.coopcycle.service.mapper.CommercantMapper;

/**
 * Integration tests for the {@link CommercantResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CommercantResourceIT {

    private static final String DEFAULT_CARTE = "AAAAAAAAAA";
    private static final String UPDATED_CARTE = "BBBBBBBBBB";

    private static final String DEFAULT_MENUS = "AAAAAAAAAA";
    private static final String UPDATED_MENUS = "BBBBBBBBBB";

    private static final Float DEFAULT_HORAIRES = 1F;
    private static final Float UPDATED_HORAIRES = 2F;

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/commercants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/commercants";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommercantRepository commercantRepository;

    @Autowired
    private CommercantMapper commercantMapper;

    /**
     * This repository is mocked in the verrimar.coopcycle.repository.search test package.
     *
     * @see verrimar.coopcycle.repository.search.CommercantSearchRepositoryMockConfiguration
     */
    @Autowired
    private CommercantSearchRepository mockCommercantSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommercantMockMvc;

    private Commercant commercant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commercant createEntity(EntityManager em) {
        Commercant commercant = new Commercant()
            .carte(DEFAULT_CARTE)
            .menus(DEFAULT_MENUS)
            .horaires(DEFAULT_HORAIRES)
            .adresse(DEFAULT_ADRESSE);
        return commercant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commercant createUpdatedEntity(EntityManager em) {
        Commercant commercant = new Commercant()
            .carte(UPDATED_CARTE)
            .menus(UPDATED_MENUS)
            .horaires(UPDATED_HORAIRES)
            .adresse(UPDATED_ADRESSE);
        return commercant;
    }

    @BeforeEach
    public void initTest() {
        commercant = createEntity(em);
    }

    @Test
    @Transactional
    void createCommercant() throws Exception {
        int databaseSizeBeforeCreate = commercantRepository.findAll().size();
        // Create the Commercant
        CommercantDTO commercantDTO = commercantMapper.toDto(commercant);
        restCommercantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commercantDTO)))
            .andExpect(status().isCreated());

        // Validate the Commercant in the database
        List<Commercant> commercantList = commercantRepository.findAll();
        assertThat(commercantList).hasSize(databaseSizeBeforeCreate + 1);
        Commercant testCommercant = commercantList.get(commercantList.size() - 1);
        assertThat(testCommercant.getCarte()).isEqualTo(DEFAULT_CARTE);
        assertThat(testCommercant.getMenus()).isEqualTo(DEFAULT_MENUS);
        assertThat(testCommercant.getHoraires()).isEqualTo(DEFAULT_HORAIRES);
        assertThat(testCommercant.getAdresse()).isEqualTo(DEFAULT_ADRESSE);

        // Validate the Commercant in Elasticsearch
        verify(mockCommercantSearchRepository, times(1)).save(testCommercant);
    }

    @Test
    @Transactional
    void createCommercantWithExistingId() throws Exception {
        // Create the Commercant with an existing ID
        commercant.setId(1L);
        CommercantDTO commercantDTO = commercantMapper.toDto(commercant);

        int databaseSizeBeforeCreate = commercantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommercantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commercantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Commercant in the database
        List<Commercant> commercantList = commercantRepository.findAll();
        assertThat(commercantList).hasSize(databaseSizeBeforeCreate);

        // Validate the Commercant in Elasticsearch
        verify(mockCommercantSearchRepository, times(0)).save(commercant);
    }

    @Test
    @Transactional
    void checkAdresseIsRequired() throws Exception {
        int databaseSizeBeforeTest = commercantRepository.findAll().size();
        // set the field null
        commercant.setAdresse(null);

        // Create the Commercant, which fails.
        CommercantDTO commercantDTO = commercantMapper.toDto(commercant);

        restCommercantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commercantDTO)))
            .andExpect(status().isBadRequest());

        List<Commercant> commercantList = commercantRepository.findAll();
        assertThat(commercantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCommercants() throws Exception {
        // Initialize the database
        commercantRepository.saveAndFlush(commercant);

        // Get all the commercantList
        restCommercantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commercant.getId().intValue())))
            .andExpect(jsonPath("$.[*].carte").value(hasItem(DEFAULT_CARTE)))
            .andExpect(jsonPath("$.[*].menus").value(hasItem(DEFAULT_MENUS)))
            .andExpect(jsonPath("$.[*].horaires").value(hasItem(DEFAULT_HORAIRES.doubleValue())))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)));
    }

    @Test
    @Transactional
    void getCommercant() throws Exception {
        // Initialize the database
        commercantRepository.saveAndFlush(commercant);

        // Get the commercant
        restCommercantMockMvc
            .perform(get(ENTITY_API_URL_ID, commercant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commercant.getId().intValue()))
            .andExpect(jsonPath("$.carte").value(DEFAULT_CARTE))
            .andExpect(jsonPath("$.menus").value(DEFAULT_MENUS))
            .andExpect(jsonPath("$.horaires").value(DEFAULT_HORAIRES.doubleValue()))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE));
    }

    @Test
    @Transactional
    void getNonExistingCommercant() throws Exception {
        // Get the commercant
        restCommercantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCommercant() throws Exception {
        // Initialize the database
        commercantRepository.saveAndFlush(commercant);

        int databaseSizeBeforeUpdate = commercantRepository.findAll().size();

        // Update the commercant
        Commercant updatedCommercant = commercantRepository.findById(commercant.getId()).get();
        // Disconnect from session so that the updates on updatedCommercant are not directly saved in db
        em.detach(updatedCommercant);
        updatedCommercant.carte(UPDATED_CARTE).menus(UPDATED_MENUS).horaires(UPDATED_HORAIRES).adresse(UPDATED_ADRESSE);
        CommercantDTO commercantDTO = commercantMapper.toDto(updatedCommercant);

        restCommercantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commercantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commercantDTO))
            )
            .andExpect(status().isOk());

        // Validate the Commercant in the database
        List<Commercant> commercantList = commercantRepository.findAll();
        assertThat(commercantList).hasSize(databaseSizeBeforeUpdate);
        Commercant testCommercant = commercantList.get(commercantList.size() - 1);
        assertThat(testCommercant.getCarte()).isEqualTo(UPDATED_CARTE);
        assertThat(testCommercant.getMenus()).isEqualTo(UPDATED_MENUS);
        assertThat(testCommercant.getHoraires()).isEqualTo(UPDATED_HORAIRES);
        assertThat(testCommercant.getAdresse()).isEqualTo(UPDATED_ADRESSE);

        // Validate the Commercant in Elasticsearch
        verify(mockCommercantSearchRepository).save(testCommercant);
    }

    @Test
    @Transactional
    void putNonExistingCommercant() throws Exception {
        int databaseSizeBeforeUpdate = commercantRepository.findAll().size();
        commercant.setId(count.incrementAndGet());

        // Create the Commercant
        CommercantDTO commercantDTO = commercantMapper.toDto(commercant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommercantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commercantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commercantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commercant in the database
        List<Commercant> commercantList = commercantRepository.findAll();
        assertThat(commercantList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Commercant in Elasticsearch
        verify(mockCommercantSearchRepository, times(0)).save(commercant);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommercant() throws Exception {
        int databaseSizeBeforeUpdate = commercantRepository.findAll().size();
        commercant.setId(count.incrementAndGet());

        // Create the Commercant
        CommercantDTO commercantDTO = commercantMapper.toDto(commercant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommercantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commercantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commercant in the database
        List<Commercant> commercantList = commercantRepository.findAll();
        assertThat(commercantList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Commercant in Elasticsearch
        verify(mockCommercantSearchRepository, times(0)).save(commercant);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommercant() throws Exception {
        int databaseSizeBeforeUpdate = commercantRepository.findAll().size();
        commercant.setId(count.incrementAndGet());

        // Create the Commercant
        CommercantDTO commercantDTO = commercantMapper.toDto(commercant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommercantMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commercantDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Commercant in the database
        List<Commercant> commercantList = commercantRepository.findAll();
        assertThat(commercantList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Commercant in Elasticsearch
        verify(mockCommercantSearchRepository, times(0)).save(commercant);
    }

    @Test
    @Transactional
    void partialUpdateCommercantWithPatch() throws Exception {
        // Initialize the database
        commercantRepository.saveAndFlush(commercant);

        int databaseSizeBeforeUpdate = commercantRepository.findAll().size();

        // Update the commercant using partial update
        Commercant partialUpdatedCommercant = new Commercant();
        partialUpdatedCommercant.setId(commercant.getId());

        partialUpdatedCommercant.carte(UPDATED_CARTE).menus(UPDATED_MENUS).horaires(UPDATED_HORAIRES);

        restCommercantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommercant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommercant))
            )
            .andExpect(status().isOk());

        // Validate the Commercant in the database
        List<Commercant> commercantList = commercantRepository.findAll();
        assertThat(commercantList).hasSize(databaseSizeBeforeUpdate);
        Commercant testCommercant = commercantList.get(commercantList.size() - 1);
        assertThat(testCommercant.getCarte()).isEqualTo(UPDATED_CARTE);
        assertThat(testCommercant.getMenus()).isEqualTo(UPDATED_MENUS);
        assertThat(testCommercant.getHoraires()).isEqualTo(UPDATED_HORAIRES);
        assertThat(testCommercant.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
    }

    @Test
    @Transactional
    void fullUpdateCommercantWithPatch() throws Exception {
        // Initialize the database
        commercantRepository.saveAndFlush(commercant);

        int databaseSizeBeforeUpdate = commercantRepository.findAll().size();

        // Update the commercant using partial update
        Commercant partialUpdatedCommercant = new Commercant();
        partialUpdatedCommercant.setId(commercant.getId());

        partialUpdatedCommercant.carte(UPDATED_CARTE).menus(UPDATED_MENUS).horaires(UPDATED_HORAIRES).adresse(UPDATED_ADRESSE);

        restCommercantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommercant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommercant))
            )
            .andExpect(status().isOk());

        // Validate the Commercant in the database
        List<Commercant> commercantList = commercantRepository.findAll();
        assertThat(commercantList).hasSize(databaseSizeBeforeUpdate);
        Commercant testCommercant = commercantList.get(commercantList.size() - 1);
        assertThat(testCommercant.getCarte()).isEqualTo(UPDATED_CARTE);
        assertThat(testCommercant.getMenus()).isEqualTo(UPDATED_MENUS);
        assertThat(testCommercant.getHoraires()).isEqualTo(UPDATED_HORAIRES);
        assertThat(testCommercant.getAdresse()).isEqualTo(UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void patchNonExistingCommercant() throws Exception {
        int databaseSizeBeforeUpdate = commercantRepository.findAll().size();
        commercant.setId(count.incrementAndGet());

        // Create the Commercant
        CommercantDTO commercantDTO = commercantMapper.toDto(commercant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommercantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commercantDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commercantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commercant in the database
        List<Commercant> commercantList = commercantRepository.findAll();
        assertThat(commercantList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Commercant in Elasticsearch
        verify(mockCommercantSearchRepository, times(0)).save(commercant);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommercant() throws Exception {
        int databaseSizeBeforeUpdate = commercantRepository.findAll().size();
        commercant.setId(count.incrementAndGet());

        // Create the Commercant
        CommercantDTO commercantDTO = commercantMapper.toDto(commercant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommercantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commercantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commercant in the database
        List<Commercant> commercantList = commercantRepository.findAll();
        assertThat(commercantList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Commercant in Elasticsearch
        verify(mockCommercantSearchRepository, times(0)).save(commercant);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommercant() throws Exception {
        int databaseSizeBeforeUpdate = commercantRepository.findAll().size();
        commercant.setId(count.incrementAndGet());

        // Create the Commercant
        CommercantDTO commercantDTO = commercantMapper.toDto(commercant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommercantMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(commercantDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Commercant in the database
        List<Commercant> commercantList = commercantRepository.findAll();
        assertThat(commercantList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Commercant in Elasticsearch
        verify(mockCommercantSearchRepository, times(0)).save(commercant);
    }

    @Test
    @Transactional
    void deleteCommercant() throws Exception {
        // Initialize the database
        commercantRepository.saveAndFlush(commercant);

        int databaseSizeBeforeDelete = commercantRepository.findAll().size();

        // Delete the commercant
        restCommercantMockMvc
            .perform(delete(ENTITY_API_URL_ID, commercant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Commercant> commercantList = commercantRepository.findAll();
        assertThat(commercantList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Commercant in Elasticsearch
        verify(mockCommercantSearchRepository, times(1)).deleteById(commercant.getId());
    }

    @Test
    @Transactional
    void searchCommercant() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        commercantRepository.saveAndFlush(commercant);
        when(mockCommercantSearchRepository.search("id:" + commercant.getId())).thenReturn(Stream.of(commercant));

        // Search the commercant
        restCommercantMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + commercant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commercant.getId().intValue())))
            .andExpect(jsonPath("$.[*].carte").value(hasItem(DEFAULT_CARTE)))
            .andExpect(jsonPath("$.[*].menus").value(hasItem(DEFAULT_MENUS)))
            .andExpect(jsonPath("$.[*].horaires").value(hasItem(DEFAULT_HORAIRES.doubleValue())))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)));
    }
}
