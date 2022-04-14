package verrimar.coopcycle.service.mapper;

import org.mapstruct.*;
import verrimar.coopcycle.domain.Commercant;
import verrimar.coopcycle.service.dto.CommercantDTO;

/**
 * Mapper for the entity {@link Commercant} and its DTO {@link CommercantDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommercantMapper extends EntityMapper<CommercantDTO, Commercant> {}
