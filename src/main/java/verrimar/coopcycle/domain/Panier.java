package verrimar.coopcycle.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Panier.
 */
@Entity
@Table(name = "panier")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "panier")
public class Panier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "montant", nullable = false)
    private Float montant;

    @NotNull
    @Column(name = "date_limite", nullable = false)
    private Float dateLimite;

    @JsonIgnoreProperties(value = { "commande", "panier" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Paiement paiement;

    @ManyToOne
    @JsonIgnoreProperties(value = { "paniers", "commandes" }, allowSetters = true)
    private Client client;

    @ManyToOne
    @JsonIgnoreProperties(value = { "paniers" }, allowSetters = true)
    private Commercant commercant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Panier id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getMontant() {
        return this.montant;
    }

    public Panier montant(Float montant) {
        this.setMontant(montant);
        return this;
    }

    public void setMontant(Float montant) {
        this.montant = montant;
    }

    public Float getDateLimite() {
        return this.dateLimite;
    }

    public Panier dateLimite(Float dateLimite) {
        this.setDateLimite(dateLimite);
        return this;
    }

    public void setDateLimite(Float dateLimite) {
        this.dateLimite = dateLimite;
    }

    public Paiement getPaiement() {
        return this.paiement;
    }

    public void setPaiement(Paiement paiement) {
        this.paiement = paiement;
    }

    public Panier paiement(Paiement paiement) {
        this.setPaiement(paiement);
        return this;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Panier client(Client client) {
        this.setClient(client);
        return this;
    }

    public Commercant getCommercant() {
        return this.commercant;
    }

    public void setCommercant(Commercant commercant) {
        this.commercant = commercant;
    }

    public Panier commercant(Commercant commercant) {
        this.setCommercant(commercant);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Panier)) {
            return false;
        }
        return id != null && id.equals(((Panier) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Panier{" +
            "id=" + getId() +
            ", montant=" + getMontant() +
            ", dateLimite=" + getDateLimite() +
            "}";
    }
}
