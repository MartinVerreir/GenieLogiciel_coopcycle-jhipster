package verrimar.coopcycle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import verrimar.coopcycle.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
