package verrimar.coopcycle.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Commercant.
 */
@Entity
@Table(name = "commercant")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "commercant")
public class Commercant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "carte")
    private String carte;

    @Column(name = "menus")
    private String menus;

    @Column(name = "horaires")
    private Float horaires;

    @NotNull
    @Column(name = "adresse", nullable = false)
    private String adresse;

    @OneToMany(mappedBy = "commercant")
    @JsonIgnoreProperties(value = { "paiement", "client", "commercant" }, allowSetters = true)
    private Set<Panier> paniers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Commercant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCarte() {
        return this.carte;
    }

    public Commercant carte(String carte) {
        this.setCarte(carte);
        return this;
    }

    public void setCarte(String carte) {
        this.carte = carte;
    }

    public String getMenus() {
        return this.menus;
    }

    public Commercant menus(String menus) {
        this.setMenus(menus);
        return this;
    }

    public void setMenus(String menus) {
        this.menus = menus;
    }

    public Float getHoraires() {
        return this.horaires;
    }

    public Commercant horaires(Float horaires) {
        this.setHoraires(horaires);
        return this;
    }

    public void setHoraires(Float horaires) {
        this.horaires = horaires;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Commercant adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Set<Panier> getPaniers() {
        return this.paniers;
    }

    public void setPaniers(Set<Panier> paniers) {
        if (this.paniers != null) {
            this.paniers.forEach(i -> i.setCommercant(null));
        }
        if (paniers != null) {
            paniers.forEach(i -> i.setCommercant(this));
        }
        this.paniers = paniers;
    }

    public Commercant paniers(Set<Panier> paniers) {
        this.setPaniers(paniers);
        return this;
    }

    public Commercant addPanier(Panier panier) {
        this.paniers.add(panier);
        panier.setCommercant(this);
        return this;
    }

    public Commercant removePanier(Panier panier) {
        this.paniers.remove(panier);
        panier.setCommercant(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Commercant)) {
            return false;
        }
        return id != null && id.equals(((Commercant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Commercant{" +
            "id=" + getId() +
            ", carte='" + getCarte() + "'" +
            ", menus='" + getMenus() + "'" +
            ", horaires=" + getHoraires() +
            ", adresse='" + getAdresse() + "'" +
            "}";
    }
}
