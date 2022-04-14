package verrimar.coopcycle.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link LivreurSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class LivreurSearchRepositoryMockConfiguration {

    @MockBean
    private LivreurSearchRepository mockLivreurSearchRepository;
}
