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
import verrimar.coopcycle.domain.Commande;
import verrimar.coopcycle.repository.CommandeRepository;
import verrimar.coopcycle.repository.search.CommandeSearchRepository;
import verrimar.coopcycle.service.dto.CommandeDTO;
import verrimar.coopcycle.service.mapper.CommandeMapper;

/**
 * Service Implementation for managing {@link Commande}.
 */
@Service
@Transactional
public class CommandeService {

    private final Logger log = LoggerFactory.getLogger(CommandeService.class);

    private final CommandeRepository commandeRepository;

    private final CommandeMapper commandeMapper;

    private final CommandeSearchRepository commandeSearchRepository;

    public CommandeService(
        CommandeRepository commandeRepository,
        CommandeMapper commandeMapper,
        CommandeSearchRepository commandeSearchRepository
    ) {
        this.commandeRepository = commandeRepository;
        this.commandeMapper = commandeMapper;
        this.commandeSearchRepository = commandeSearchRepository;
    }

    /**
     * Save a commande.
     *
     * @param commandeDTO the entity to save.
     * @return the persisted entity.
     */
    public CommandeDTO save(CommandeDTO commandeDTO) {
        log.debug("Request to save Commande : {}", commandeDTO);
        Commande commande = commandeMapper.toEntity(commandeDTO);
        commande = commandeRepository.save(commande);
        CommandeDTO result = commandeMapper.toDto(commande);
        commandeSearchRepository.save(commande);
        return result;
    }

    /**
     * Update a commande.
     *
     * @param commandeDTO the entity to save.
     * @return the persisted entity.
     */
    public CommandeDTO update(CommandeDTO commandeDTO) {
        log.debug("Request to save Commande : {}", commandeDTO);
        Commande commande = commandeMapper.toEntity(commandeDTO);
        commande = commandeRepository.save(commande);
        CommandeDTO result = commandeMapper.toDto(commande);
        commandeSearchRepository.save(commande);
        return result;
    }

    /**
     * Partially update a commande.
     *
     * @param commandeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CommandeDTO> partialUpdate(CommandeDTO commandeDTO) {
        log.debug("Request to partially update Commande : {}", commandeDTO);

        return commandeRepository
            .findById(commandeDTO.getId())
            .map(existingCommande -> {
                commandeMapper.partialUpdate(existingCommande, commandeDTO);

                return existingCommande;
            })
            .map(commandeRepository::save)
            .map(savedCommande -> {
                commandeSearchRepository.save(savedCommande);

                return savedCommande;
            })
            .map(commandeMapper::toDto);
    }

    /**
     * Get all the commandes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CommandeDTO> findAll() {
        log.debug("Request to get all Commandes");
        return commandeRepository.findAll().stream().map(commandeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the commandes where Paiement is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CommandeDTO> findAllWherePaiementIsNull() {
        log.debug("Request to get all commandes where Paiement is null");
        return StreamSupport
            .stream(commandeRepository.findAll().spliterator(), false)
            .filter(commande -> commande.getPaiement() == null)
            .map(commandeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one commande by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CommandeDTO> findOne(Long id) {
        log.debug("Request to get Commande : {}", id);
        return commandeRepository.findById(id).map(commandeMapper::toDto);
    }

    /**
     * Delete the commande by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Commande : {}", id);
        commandeRepository.deleteById(id);
        commandeSearchRepository.deleteById(id);
    }

    /**
     * Search for the commande corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CommandeDTO> search(String query) {
        log.debug("Request to search Commandes for query {}", query);
        return StreamSupport
            .stream(commandeSearchRepository.search(query).spliterator(), false)
            .map(commandeMapper::toDto)
            .collect(Collectors.toList());
    }
}
