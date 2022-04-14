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
import verrimar.coopcycle.domain.Paiement;
import verrimar.coopcycle.repository.PaiementRepository;
import verrimar.coopcycle.repository.search.PaiementSearchRepository;
import verrimar.coopcycle.service.dto.PaiementDTO;
import verrimar.coopcycle.service.mapper.PaiementMapper;

/**
 * Service Implementation for managing {@link Paiement}.
 */
@Service
@Transactional
public class PaiementService {

    private final Logger log = LoggerFactory.getLogger(PaiementService.class);

    private final PaiementRepository paiementRepository;

    private final PaiementMapper paiementMapper;

    private final PaiementSearchRepository paiementSearchRepository;

    public PaiementService(
        PaiementRepository paiementRepository,
        PaiementMapper paiementMapper,
        PaiementSearchRepository paiementSearchRepository
    ) {
        this.paiementRepository = paiementRepository;
        this.paiementMapper = paiementMapper;
        this.paiementSearchRepository = paiementSearchRepository;
    }

    /**
     * Save a paiement.
     *
     * @param paiementDTO the entity to save.
     * @return the persisted entity.
     */
    public PaiementDTO save(PaiementDTO paiementDTO) {
        log.debug("Request to save Paiement : {}", paiementDTO);
        Paiement paiement = paiementMapper.toEntity(paiementDTO);
        paiement = paiementRepository.save(paiement);
        PaiementDTO result = paiementMapper.toDto(paiement);
        paiementSearchRepository.save(paiement);
        return result;
    }

    /**
     * Update a paiement.
     *
     * @param paiementDTO the entity to save.
     * @return the persisted entity.
     */
    public PaiementDTO update(PaiementDTO paiementDTO) {
        log.debug("Request to save Paiement : {}", paiementDTO);
        Paiement paiement = paiementMapper.toEntity(paiementDTO);
        paiement = paiementRepository.save(paiement);
        PaiementDTO result = paiementMapper.toDto(paiement);
        paiementSearchRepository.save(paiement);
        return result;
    }

    /**
     * Partially update a paiement.
     *
     * @param paiementDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaiementDTO> partialUpdate(PaiementDTO paiementDTO) {
        log.debug("Request to partially update Paiement : {}", paiementDTO);

        return paiementRepository
            .findById(paiementDTO.getId())
            .map(existingPaiement -> {
                paiementMapper.partialUpdate(existingPaiement, paiementDTO);

                return existingPaiement;
            })
            .map(paiementRepository::save)
            .map(savedPaiement -> {
                paiementSearchRepository.save(savedPaiement);

                return savedPaiement;
            })
            .map(paiementMapper::toDto);
    }

    /**
     * Get all the paiements.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PaiementDTO> findAll() {
        log.debug("Request to get all Paiements");
        return paiementRepository.findAll().stream().map(paiementMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the paiements where Panier is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PaiementDTO> findAllWherePanierIsNull() {
        log.debug("Request to get all paiements where Panier is null");
        return StreamSupport
            .stream(paiementRepository.findAll().spliterator(), false)
            .filter(paiement -> paiement.getPanier() == null)
            .map(paiementMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one paiement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaiementDTO> findOne(Long id) {
        log.debug("Request to get Paiement : {}", id);
        return paiementRepository.findById(id).map(paiementMapper::toDto);
    }

    /**
     * Delete the paiement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Paiement : {}", id);
        paiementRepository.deleteById(id);
        paiementSearchRepository.deleteById(id);
    }

    /**
     * Search for the paiement corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PaiementDTO> search(String query) {
        log.debug("Request to search Paiements for query {}", query);
        return StreamSupport
            .stream(paiementSearchRepository.search(query).spliterator(), false)
            .map(paiementMapper::toDto)
            .collect(Collectors.toList());
    }
}
