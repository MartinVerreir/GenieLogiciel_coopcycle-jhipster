package verrimar.coopcycle.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import verrimar.coopcycle.repository.LivreurRepository;
import verrimar.coopcycle.service.LivreurService;
import verrimar.coopcycle.service.dto.LivreurDTO;
import verrimar.coopcycle.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link verrimar.coopcycle.domain.Livreur}.
 */
@RestController
@RequestMapping("/api")
public class LivreurResource {

    private final Logger log = LoggerFactory.getLogger(LivreurResource.class);

    private static final String ENTITY_NAME = "livreur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LivreurService livreurService;

    private final LivreurRepository livreurRepository;

    public LivreurResource(LivreurService livreurService, LivreurRepository livreurRepository) {
        this.livreurService = livreurService;
        this.livreurRepository = livreurRepository;
    }

    /**
     * {@code POST  /livreurs} : Create a new livreur.
     *
     * @param livreurDTO the livreurDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new livreurDTO, or with status {@code 400 (Bad Request)} if the livreur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/livreurs")
    public ResponseEntity<LivreurDTO> createLivreur(@Valid @RequestBody LivreurDTO livreurDTO) throws URISyntaxException {
        log.debug("REST request to save Livreur : {}", livreurDTO);
        if (livreurDTO.getId() != null) {
            throw new BadRequestAlertException("A new livreur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LivreurDTO result = livreurService.save(livreurDTO);
        return ResponseEntity
            .created(new URI("/api/livreurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /livreurs/:id} : Updates an existing livreur.
     *
     * @param id the id of the livreurDTO to save.
     * @param livreurDTO the livreurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated livreurDTO,
     * or with status {@code 400 (Bad Request)} if the livreurDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the livreurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/livreurs/{id}")
    public ResponseEntity<LivreurDTO> updateLivreur(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LivreurDTO livreurDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Livreur : {}, {}", id, livreurDTO);
        if (livreurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, livreurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!livreurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LivreurDTO result = livreurService.update(livreurDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, livreurDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /livreurs/:id} : Partial updates given fields of an existing livreur, field will ignore if it is null
     *
     * @param id the id of the livreurDTO to save.
     * @param livreurDTO the livreurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated livreurDTO,
     * or with status {@code 400 (Bad Request)} if the livreurDTO is not valid,
     * or with status {@code 404 (Not Found)} if the livreurDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the livreurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/livreurs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LivreurDTO> partialUpdateLivreur(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LivreurDTO livreurDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Livreur partially : {}, {}", id, livreurDTO);
        if (livreurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, livreurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!livreurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LivreurDTO> result = livreurService.partialUpdate(livreurDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, livreurDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /livreurs} : get all the livreurs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of livreurs in body.
     */
    @GetMapping("/livreurs")
    public List<LivreurDTO> getAllLivreurs() {
        log.debug("REST request to get all Livreurs");
        return livreurService.findAll();
    }

    /**
     * {@code GET  /livreurs/:id} : get the "id" livreur.
     *
     * @param id the id of the livreurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the livreurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/livreurs/{id}")
    public ResponseEntity<LivreurDTO> getLivreur(@PathVariable Long id) {
        log.debug("REST request to get Livreur : {}", id);
        Optional<LivreurDTO> livreurDTO = livreurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(livreurDTO);
    }

    /**
     * {@code DELETE  /livreurs/:id} : delete the "id" livreur.
     *
     * @param id the id of the livreurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/livreurs/{id}")
    public ResponseEntity<Void> deleteLivreur(@PathVariable Long id) {
        log.debug("REST request to delete Livreur : {}", id);
        livreurService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/livreurs?query=:query} : search for the livreur corresponding
     * to the query.
     *
     * @param query the query of the livreur search.
     * @return the result of the search.
     */
    @GetMapping("/_search/livreurs")
    public List<LivreurDTO> searchLivreurs(@RequestParam String query) {
        log.debug("REST request to search Livreurs for query {}", query);
        return livreurService.search(query);
    }
}
