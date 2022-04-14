package verrimar.coopcycle.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import verrimar.coopcycle.domain.Panier;

/**
 * Spring Data SQL repository for the Panier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PanierRepository extends JpaRepository<Panier, Long> {}
