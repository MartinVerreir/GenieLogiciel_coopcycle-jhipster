package verrimar.coopcycle.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import verrimar.coopcycle.domain.Panier;
import verrimar.coopcycle.repository.PanierRepository;
import verrimar.coopcycle.repository.search.PanierSearchRepository;
import verrimar.coopcycle.service.dto.PanierDTO;
import verrimar.coopcycle.service.mapper.PanierMapper;

/**
 * Service Implementation for managing {@link Panier}.
 */
@Service
@Transactional
public class PanierService {

    private final Logger log = LoggerFactory.getLogger(PanierService.class);

    private final PanierRepository panierRepository;

    private final PanierMapper panierMapper;

    private final PanierSearchRepository panierSearchRepository;

    public PanierService(PanierRepository panierRepository, PanierMapper panierMapper, PanierSearchRepository panierSearchRepository) {
        this.panierRepository = panierRepository;
        this.panierMapper = panierMapper;
        this.panierSearchRepository = panierSearchRepository;
    }

    /**
     * Save a panier.
     *
     * @param panierDTO the entity to save.
     * @return the persisted entity.
     */
    public PanierDTO save(PanierDTO panierDTO) {
        log.debug("Request to save Panier : {}", panierDTO);
        Panier panier = panierMapper.toEntity(panierDTO);
        panier = panierRepository.save(panier);
        PanierDTO result = panierMapper.toDto(panier);
        panierSearchRepository.save(panier);
        return result;
    }

    /**
     * Update a panier.
     *
     * @param panierDTO the entity to save.
     * @return the persisted entity.
     */
    public PanierDTO update(PanierDTO panierDTO) {
        log.debug("Request to save Panier : {}", panierDTO);
        Panier panier = panierMapper.toEntity(panierDTO);
        panier = panierRepository.save(panier);
        PanierDTO result = panierMapper.toDto(panier);
        panierSearchRepository.save(panier);
        return result;
    }

    /**
     * Partially update a panier.
     *
     * @param panierDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PanierDTO> partialUpdate(PanierDTO panierDTO) {
        log.debug("Request to partially update Panier : {}", panierDTO);

        return panierRepository
            .findById(panierDTO.getId())
            .map(existingPanier -> {
                panierMapper.partialUpdate(existingPanier, panierDTO);

                return existingPanier;
            })
            .map(panierRepository::save)
            .map(savedPanier -> {
                panierSearchRepository.save(savedPanier);

                return savedPanier;
            })
            .map(panierMapper::toDto);
    }

    /**
     * Get all the paniers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PanierDTO> findAll() {
        log.debug("Request to get all Paniers");
        return panierRepository.findAll().stream().map(panierMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one panier by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PanierDTO> findOne(Long id) {
        log.debug("Request to get Panier : {}", id);
        return panierRepository.findById(id).map(panierMapper::toDto);
    }

    /**
     * Delete the panier by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Panier : {}", id);
        panierRepository.deleteById(id);
        panierSearchRepository.deleteById(id);
    }

    /**
     * Search for the panier corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PanierDTO> search(String query) {
        log.debug("Request to search Paniers for query {}", query);
        return StreamSupport
            .stream(panierSearchRepository.search(query).spliterator(), false)
            .map(panierMapper::toDto)
            .collect(Collectors.toList());
    }
}
