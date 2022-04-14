package verrimar.coopcycle.service.mapper;

import org.mapstruct.*;
import verrimar.coopcycle.domain.Client;
import verrimar.coopcycle.service.dto.ClientDTO;

/**
 * Mapper for the entity {@link Client} and its DTO {@link ClientDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClientMapper extends EntityMapper<ClientDTO, Client> {}
