package verrimar.coopcycle.service.mapper;

import org.mapstruct.*;
import verrimar.coopcycle.domain.Commande;
import verrimar.coopcycle.domain.Paiement;
import verrimar.coopcycle.service.dto.CommandeDTO;
import verrimar.coopcycle.service.dto.PaiementDTO;

/**
 * Mapper for the entity {@link Paiement} and its DTO {@link PaiementDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaiementMapper extends EntityMapper<PaiementDTO, Paiement> {
    @Mapping(target = "commande", source = "commande", qualifiedByName = "commandeId")
    PaiementDTO toDto(Paiement s);

    @Named("commandeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CommandeDTO toDtoCommandeId(Commande commande);
}
