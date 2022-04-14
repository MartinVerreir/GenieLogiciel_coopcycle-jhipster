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
import verrimar.coopcycle.domain.Cooperative;
import verrimar.coopcycle.repository.CooperativeRepository;
import verrimar.coopcycle.repository.search.CooperativeSearchRepository;
import verrimar.coopcycle.service.dto.CooperativeDTO;
import verrimar.coopcycle.service.mapper.CooperativeMapper;

/**
 * Service Implementation for managing {@link Cooperative}.
 */
@Service
@Transactional
public class CooperativeService {

    private final Logger log = LoggerFactory.getLogger(CooperativeService.class);

    private final CooperativeRepository cooperativeRepository;

    private final CooperativeMapper cooperativeMapper;

    private final CooperativeSearchRepository cooperativeSearchRepository;

    public CooperativeService(
        CooperativeRepository cooperativeRepository,
        CooperativeMapper cooperativeMapper,
        CooperativeSearchRepository cooperativeSearchRepository
    ) {
        this.cooperativeRepository = cooperativeRepository;
        this.cooperativeMapper = cooperativeMapper;
        this.cooperativeSearchRepository = cooperativeSearchRepository;
    }

    /**
     * Save a cooperative.
     *
     * @param cooperativeDTO the entity to save.
     * @return the persisted entity.
     */
    public CooperativeDTO save(CooperativeDTO cooperativeDTO) {
        log.debug("Request to save Cooperative : {}", cooperativeDTO);
        Cooperative cooperative = cooperativeMapper.toEntity(cooperativeDTO);
        cooperative = cooperativeRepository.save(cooperative);
        CooperativeDTO result = cooperativeMapper.toDto(cooperative);
        cooperativeSearchRepository.save(cooperative);
        return result;
    }

    /**
     * Update a cooperative.
     *
     * @param cooperativeDTO the entity to save.
     * @return the persisted entity.
     */
    public CooperativeDTO update(CooperativeDTO cooperativeDTO) {
        log.debug("Request to save Cooperative : {}", cooperativeDTO);
        Cooperative cooperative = cooperativeMapper.toEntity(cooperativeDTO);
        cooperative = cooperativeRepository.save(cooperative);
        CooperativeDTO result = cooperativeMapper.toDto(cooperative);
        cooperativeSearchRepository.save(cooperative);
        return result;
    }

    /**
     * Partially update a cooperative.
     *
     * @param cooperativeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CooperativeDTO> partialUpdate(CooperativeDTO cooperativeDTO) {
        log.debug("Request to partially update Cooperative : {}", cooperativeDTO);

        return cooperativeRepository
            .findById(cooperativeDTO.getId())
            .map(existingCooperative -> {
                cooperativeMapper.partialUpdate(existingCooperative, cooperativeDTO);

                return existingCooperative;
            })
            .map(cooperativeRepository::save)
            .map(savedCooperative -> {
                cooperativeSearchRepository.save(savedCooperative);

                return savedCooperative;
            })
            .map(cooperativeMapper::toDto);
    }

    /**
     * Get all the cooperatives.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CooperativeDTO> findAll() {
        log.debug("Request to get all Cooperatives");
        return cooperativeRepository.findAll().stream().map(cooperativeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one cooperative by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CooperativeDTO> findOne(Long id) {
        log.debug("Request to get Cooperative : {}", id);
        return cooperativeRepository.findById(id).map(cooperativeMapper::toDto);
    }

    /**
     * Delete the cooperative by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Cooperative : {}", id);
        cooperativeRepository.deleteById(id);
        cooperativeSearchRepository.deleteById(id);
    }

    /**
     * Search for the cooperative corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CooperativeDTO> search(String query) {
        log.debug("Request to search Cooperatives for query {}", query);
        return StreamSupport
            .stream(cooperativeSearchRepository.search(query).spliterator(), false)
            .map(cooperativeMapper::toDto)
            .collect(Collectors.toList());
    }
}
