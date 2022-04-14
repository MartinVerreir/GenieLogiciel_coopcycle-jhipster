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
import verrimar.coopcycle.domain.Commercant;
import verrimar.coopcycle.repository.CommercantRepository;
import verrimar.coopcycle.repository.search.CommercantSearchRepository;
import verrimar.coopcycle.service.dto.CommercantDTO;
import verrimar.coopcycle.service.mapper.CommercantMapper;

/**
 * Service Implementation for managing {@link Commercant}.
 */
@Service
@Transactional
public class CommercantService {

    private final Logger log = LoggerFactory.getLogger(CommercantService.class);

    private final CommercantRepository commercantRepository;

    private final CommercantMapper commercantMapper;

    private final CommercantSearchRepository commercantSearchRepository;

    public CommercantService(
        CommercantRepository commercantRepository,
        CommercantMapper commercantMapper,
        CommercantSearchRepository commercantSearchRepository
    ) {
        this.commercantRepository = commercantRepository;
        this.commercantMapper = commercantMapper;
        this.commercantSearchRepository = commercantSearchRepository;
    }

    /**
     * Save a commercant.
     *
     * @param commercantDTO the entity to save.
     * @return the persisted entity.
     */
    public CommercantDTO save(CommercantDTO commercantDTO) {
        log.debug("Request to save Commercant : {}", commercantDTO);
        Commercant commercant = commercantMapper.toEntity(commercantDTO);
        commercant = commercantRepository.save(commercant);
        CommercantDTO result = commercantMapper.toDto(commercant);
        commercantSearchRepository.save(commercant);
        return result;
    }

    /**
     * Update a commercant.
     *
     * @param commercantDTO the entity to save.
     * @return the persisted entity.
     */
    public CommercantDTO update(CommercantDTO commercantDTO) {
        log.debug("Request to save Commercant : {}", commercantDTO);
        Commercant commercant = commercantMapper.toEntity(commercantDTO);
        commercant = commercantRepository.save(commercant);
        CommercantDTO result = commercantMapper.toDto(commercant);
        commercantSearchRepository.save(commercant);
        return result;
    }

    /**
     * Partially update a commercant.
     *
     * @param commercantDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CommercantDTO> partialUpdate(CommercantDTO commercantDTO) {
        log.debug("Request to partially update Commercant : {}", commercantDTO);

        return commercantRepository
            .findById(commercantDTO.getId())
            .map(existingCommercant -> {
                commercantMapper.partialUpdate(existingCommercant, commercantDTO);

                return existingCommercant;
            })
            .map(commercantRepository::save)
            .map(savedCommercant -> {
                commercantSearchRepository.save(savedCommercant);

                return savedCommercant;
            })
            .map(commercantMapper::toDto);
    }

    /**
     * Get all the commercants.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CommercantDTO> findAll() {
        log.debug("Request to get all Commercants");
        return commercantRepository.findAll().stream().map(commercantMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one commercant by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CommercantDTO> findOne(Long id) {
        log.debug("Request to get Commercant : {}", id);
        return commercantRepository.findById(id).map(commercantMapper::toDto);
    }

    /**
     * Delete the commercant by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Commercant : {}", id);
        commercantRepository.deleteById(id);
        commercantSearchRepository.deleteById(id);
    }

    /**
     * Search for the commercant corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CommercantDTO> search(String query) {
        log.debug("Request to search Commercants for query {}", query);
        return StreamSupport
            .stream(commercantSearchRepository.search(query).spliterator(), false)
            .map(commercantMapper::toDto)
            .collect(Collectors.toList());
    }
}
