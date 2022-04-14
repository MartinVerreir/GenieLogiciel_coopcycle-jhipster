package verrimar.coopcycle.service.mapper;

import org.mapstruct.*;
import verrimar.coopcycle.domain.Client;
import verrimar.coopcycle.domain.Commercant;
import verrimar.coopcycle.domain.Paiement;
import verrimar.coopcycle.domain.Panier;
import verrimar.coopcycle.service.dto.ClientDTO;
import verrimar.coopcycle.service.dto.CommercantDTO;
import verrimar.coopcycle.service.dto.PaiementDTO;
import verrimar.coopcycle.service.dto.PanierDTO;

/**
 * Mapper for the entity {@link Panier} and its DTO {@link PanierDTO}.
 */
@Mapper(componentModel = "spring")
public interface PanierMapper extends EntityMapper<PanierDTO, Panier> {
    @Mapping(target = "paiement", source = "paiement", qualifiedByName = "paiementId")
    @Mapping(target = "client", source = "client", qualifiedByName = "clientId")
    @Mapping(target = "commercant", source = "commercant", qualifiedByName = "commercantId")
    PanierDTO toDto(Panier s);

    @Named("paiementId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaiementDTO toDtoPaiementId(Paiement paiement);

    @Named("clientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClientDTO toDtoClientId(Client client);

    @Named("commercantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CommercantDTO toDtoCommercantId(Commercant commercant);
}
