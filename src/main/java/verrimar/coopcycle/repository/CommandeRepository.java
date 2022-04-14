package verrimar.coopcycle.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import verrimar.coopcycle.domain.Commande;

/**
 * Spring Data SQL repository for the Commande entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {}
