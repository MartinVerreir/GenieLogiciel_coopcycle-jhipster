package verrimar.coopcycle.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import verrimar.coopcycle.domain.Cooperative;

/**
 * Spring Data SQL repository for the Cooperative entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CooperativeRepository extends JpaRepository<Cooperative, Long> {}
