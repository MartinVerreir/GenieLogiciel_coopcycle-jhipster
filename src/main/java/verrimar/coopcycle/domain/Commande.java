package verrimar.coopcycle.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Commande.
 */
@Entity
@Table(name = "commande")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "commande")
public class Commande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "adresse_livraison", nullable = false)
    private String adresseLivraison;

    @NotNull
    @Column(name = "echeance", nullable = false)
    private Float echeance;

    @JsonIgnoreProperties(value = { "commande", "panier" }, allowSetters = true)
    @OneToOne(mappedBy = "commande")
    private Paiement paiement;

    @ManyToOne
    @JsonIgnoreProperties(value = { "commandes", "cooperative" }, allowSetters = true)
    private Livreur livreur;

    @ManyToOne
    @JsonIgnoreProperties(value = { "paniers", "commandes" }, allowSetters = true)
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Commande id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdresseLivraison() {
        return this.adresseLivraison;
    }

    public Commande adresseLivraison(String adresseLivraison) {
        this.setAdresseLivraison(adresseLivraison);
        return this;
    }

    public void setAdresseLivraison(String adresseLivraison) {
        this.adresseLivraison = adresseLivraison;
    }

    public Float getEcheance() {
        return this.echeance;
    }

    public Commande echeance(Float echeance) {
        this.setEcheance(echeance);
        return this;
    }

    public void setEcheance(Float echeance) {
        this.echeance = echeance;
    }

    public Paiement getPaiement() {
        return this.paiement;
    }

    public void setPaiement(Paiement paiement) {
        if (this.paiement != null) {
            this.paiement.setCommande(null);
        }
        if (paiement != null) {
            paiement.setCommande(this);
        }
        this.paiement = paiement;
    }

    public Commande paiement(Paiement paiement) {
        this.setPaiement(paiement);
        return this;
    }

    public Livreur getLivreur() {
        return this.livreur;
    }

    public void setLivreur(Livreur livreur) {
        this.livreur = livreur;
    }

    public Commande livreur(Livreur livreur) {
        this.setLivreur(livreur);
        return this;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Commande client(Client client) {
        this.setClient(client);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Commande)) {
            return false;
        }
        return id != null && id.equals(((Commande) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Commande{" +
            "id=" + getId() +
            ", adresseLivraison='" + getAdresseLivraison() + "'" +
            ", echeance=" + getEcheance() +
            "}";
    }
}
