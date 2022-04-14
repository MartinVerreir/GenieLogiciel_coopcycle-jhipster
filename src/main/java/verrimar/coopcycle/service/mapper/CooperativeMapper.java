package verrimar.coopcycle.service.mapper;

import org.mapstruct.*;
import verrimar.coopcycle.domain.Cooperative;
import verrimar.coopcycle.service.dto.CooperativeDTO;

/**
 * Mapper for the entity {@link Cooperative} and its DTO {@link CooperativeDTO}.
 */
@Mapper(componentModel = "spring")
public interface CooperativeMapper extends EntityMapper<CooperativeDTO, Cooperative> {}
