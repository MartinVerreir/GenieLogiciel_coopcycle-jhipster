package verrimar.coopcycle.service.mapper;

import org.mapstruct.*;
import verrimar.coopcycle.domain.Cooperative;
import verrimar.coopcycle.domain.Livreur;
import verrimar.coopcycle.service.dto.CooperativeDTO;
import verrimar.coopcycle.service.dto.LivreurDTO;

/**
 * Mapper for the entity {@link Livreur} and its DTO {@link LivreurDTO}.
 */
@Mapper(componentModel = "spring")
public interface LivreurMapper extends EntityMapper<LivreurDTO, Livreur> {
    @Mapping(target = "cooperative", source = "cooperative", qualifiedByName = "cooperativeId")
    LivreurDTO toDto(Livreur s);

    @Named("cooperativeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CooperativeDTO toDtoCooperativeId(Cooperative cooperative);
}
