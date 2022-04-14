package verrimar.coopcycle.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import verrimar.coopcycle.domain.Client;

/**
 * Spring Data SQL repository for the Client entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {}
