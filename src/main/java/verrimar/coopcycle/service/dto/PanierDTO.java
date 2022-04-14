package verrimar.coopcycle.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link verrimar.coopcycle.domain.Panier} entity.
 */
public class PanierDTO implements Serializable {

    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    private Float montant;

    @NotNull
    private Float dateLimite;

    private PaiementDTO paiement;

    private ClientDTO client;

    private CommercantDTO commercant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getMontant() {
        return montant;
    }

    public void setMontant(Float montant) {
        this.montant = montant;
    }

    public Float getDateLimite() {
        return dateLimite;
    }

    public void setDateLimite(Float dateLimite) {
        this.dateLimite = dateLimite;
    }

    public PaiementDTO getPaiement() {
        return paiement;
    }

    public void setPaiement(PaiementDTO paiement) {
        this.paiement = paiement;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public CommercantDTO getCommercant() {
        return commercant;
    }

    public void setCommercant(CommercantDTO commercant) {
        this.commercant = commercant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PanierDTO)) {
            return false;
        }

        PanierDTO panierDTO = (PanierDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, panierDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PanierDTO{" +
            "id=" + getId() +
            ", montant=" + getMontant() +
            ", dateLimite=" + getDateLimite() +
            ", paiement=" + getPaiement() +
            ", client=" + getClient() +
            ", commercant=" + getCommercant() +
            "}";
    }
}
