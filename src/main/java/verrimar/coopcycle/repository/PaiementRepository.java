package verrimar.coopcycle.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import verrimar.coopcycle.domain.Paiement;

/**
 * Spring Data SQL repository for the Paiement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {}
