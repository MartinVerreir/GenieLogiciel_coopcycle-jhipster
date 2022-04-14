package verrimar.coopcycle.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link verrimar.coopcycle.domain.Commercant} entity.
 */
public class CommercantDTO implements Serializable {

    private Long id;

    private String carte;

    private String menus;

    private Float horaires;

    @NotNull
    private String adresse;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCarte() {
        return carte;
    }

    public void setCarte(String carte) {
        this.carte = carte;
    }

    public String getMenus() {
        return menus;
    }

    public void setMenus(String menus) {
        this.menus = menus;
    }

    public Float getHoraires() {
        return horaires;
    }

    public void setHoraires(Float horaires) {
        this.horaires = horaires;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommercantDTO)) {
            return false;
        }

        CommercantDTO commercantDTO = (CommercantDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commercantDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommercantDTO{" +
            "id=" + getId() +
            ", carte='" + getCarte() + "'" +
            ", menus='" + getMenus() + "'" +
            ", horaires=" + getHoraires() +
            ", adresse='" + getAdresse() + "'" +
            "}";
    }
}
