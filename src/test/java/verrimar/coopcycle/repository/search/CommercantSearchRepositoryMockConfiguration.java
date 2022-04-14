package verrimar.coopcycle.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link CommercantSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CommercantSearchRepositoryMockConfiguration {

    @MockBean
    private CommercantSearchRepository mockCommercantSearchRepository;
}
