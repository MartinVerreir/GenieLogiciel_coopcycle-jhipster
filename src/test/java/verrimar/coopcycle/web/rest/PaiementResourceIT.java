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
import verrimar.coopcycle.domain.Paiement;
import verrimar.coopcycle.repository.PaiementRepository;
import verrimar.coopcycle.repository.search.PaiementSearchRepository;
import verrimar.coopcycle.service.dto.PaiementDTO;
import verrimar.coopcycle.service.mapper.PaiementMapper;

/**
 * Integration tests for the {@link PaiementResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PaiementResourceIT {

    private static final Float DEFAULT_MONTANT = 0F;
    private static final Float UPDATED_MONTANT = 1F;

    private static final String ENTITY_API_URL = "/api/paiements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/paiements";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaiementRepository paiementRepository;

    @Autowired
    private PaiementMapper paiementMapper;

    /**
     * This repository is mocked in the verrimar.coopcycle.repository.search test package.
     *
     * @see verrimar.coopcycle.repository.search.PaiementSearchRepositoryMockConfiguration
     */
    @Autowired
    private PaiementSearchRepository mockPaiementSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaiementMockMvc;

    private Paiement paiement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paiement createEntity(EntityManager em) {
        Paiement paiement = new Paiement().montant(DEFAULT_MONTANT);
        return paiement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paiement createUpdatedEntity(EntityManager em) {
        Paiement paiement = new Paiement().montant(UPDATED_MONTANT);
        return paiement;
    }

    @BeforeEach
    public void initTest() {
        paiement = createEntity(em);
    }

    @Test
    @Transactional
    void createPaiement() throws Exception {
        int databaseSizeBeforeCreate = paiementRepository.findAll().size();
        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);
        restPaiementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paiementDTO)))
            .andExpect(status().isCreated());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeCreate + 1);
        Paiement testPaiement = paiementList.get(paiementList.size() - 1);
        assertThat(testPaiement.getMontant()).isEqualTo(DEFAULT_MONTANT);

        // Validate the Paiement in Elasticsearch
        verify(mockPaiementSearchRepository, times(1)).save(testPaiement);
    }

    @Test
    @Transactional
    void createPaiementWithExistingId() throws Exception {
        // Create the Paiement with an existing ID
        paiement.setId(1L);
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        int databaseSizeBeforeCreate = paiementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaiementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paiementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeCreate);

        // Validate the Paiement in Elasticsearch
        verify(mockPaiementSearchRepository, times(0)).save(paiement);
    }

    @Test
    @Transactional
    void checkMontantIsRequired() throws Exception {
        int databaseSizeBeforeTest = paiementRepository.findAll().size();
        // set the field null
        paiement.setMontant(null);

        // Create the Paiement, which fails.
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        restPaiementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paiementDTO)))
            .andExpect(status().isBadRequest());

        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaiements() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList
        restPaiementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paiement.getId().intValue())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())));
    }

    @Test
    @Transactional
    void getPaiement() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get the paiement
        restPaiementMockMvc
            .perform(get(ENTITY_API_URL_ID, paiement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paiement.getId().intValue()))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingPaiement() throws Exception {
        // Get the paiement
        restPaiementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPaiement() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();

        // Update the paiement
        Paiement updatedPaiement = paiementRepository.findById(paiement.getId()).get();
        // Disconnect from session so that the updates on updatedPaiement are not directly saved in db
        em.detach(updatedPaiement);
        updatedPaiement.montant(UPDATED_MONTANT);
        PaiementDTO paiementDTO = paiementMapper.toDto(updatedPaiement);

        restPaiementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paiementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementDTO))
            )
            .andExpect(status().isOk());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);
        Paiement testPaiement = paiementList.get(paiementList.size() - 1);
        assertThat(testPaiement.getMontant()).isEqualTo(UPDATED_MONTANT);

        // Validate the Paiement in Elasticsearch
        verify(mockPaiementSearchRepository).save(testPaiement);
    }

    @Test
    @Transactional
    void putNonExistingPaiement() throws Exception {
        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();
        paiement.setId(count.incrementAndGet());

        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paiementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Paiement in Elasticsearch
        verify(mockPaiementSearchRepository, times(0)).save(paiement);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaiement() throws Exception {
        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();
        paiement.setId(count.incrementAndGet());

        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Paiement in Elasticsearch
        verify(mockPaiementSearchRepository, times(0)).save(paiement);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaiement() throws Exception {
        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();
        paiement.setId(count.incrementAndGet());

        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paiementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Paiement in Elasticsearch
        verify(mockPaiementSearchRepository, times(0)).save(paiement);
    }

    @Test
    @Transactional
    void partialUpdatePaiementWithPatch() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();

        // Update the paiement using partial update
        Paiement partialUpdatedPaiement = new Paiement();
        partialUpdatedPaiement.setId(paiement.getId());

        restPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaiement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaiement))
            )
            .andExpect(status().isOk());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);
        Paiement testPaiement = paiementList.get(paiementList.size() - 1);
        assertThat(testPaiement.getMontant()).isEqualTo(DEFAULT_MONTANT);
    }

    @Test
    @Transactional
    void fullUpdatePaiementWithPatch() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();

        // Update the paiement using partial update
        Paiement partialUpdatedPaiement = new Paiement();
        partialUpdatedPaiement.setId(paiement.getId());

        partialUpdatedPaiement.montant(UPDATED_MONTANT);

        restPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaiement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaiement))
            )
            .andExpect(status().isOk());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);
        Paiement testPaiement = paiementList.get(paiementList.size() - 1);
        assertThat(testPaiement.getMontant()).isEqualTo(UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void patchNonExistingPaiement() throws Exception {
        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();
        paiement.setId(count.incrementAndGet());

        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paiementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paiementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Paiement in Elasticsearch
        verify(mockPaiementSearchRepository, times(0)).save(paiement);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaiement() throws Exception {
        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();
        paiement.setId(count.incrementAndGet());

        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paiementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Paiement in Elasticsearch
        verify(mockPaiementSearchRepository, times(0)).save(paiement);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaiement() throws Exception {
        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();
        paiement.setId(count.incrementAndGet());

        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(paiementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Paiement in Elasticsearch
        verify(mockPaiementSearchRepository, times(0)).save(paiement);
    }

    @Test
    @Transactional
    void deletePaiement() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        int databaseSizeBeforeDelete = paiementRepository.findAll().size();

        // Delete the paiement
        restPaiementMockMvc
            .perform(delete(ENTITY_API_URL_ID, paiement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Paiement in Elasticsearch
        verify(mockPaiementSearchRepository, times(1)).deleteById(paiement.getId());
    }

    @Test
    @Transactional
    void searchPaiement() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);
        when(mockPaiementSearchRepository.search("id:" + paiement.getId())).thenReturn(Stream.of(paiement));

        // Search the paiement
        restPaiementMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + paiement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paiement.getId().intValue())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())));
    }
}
