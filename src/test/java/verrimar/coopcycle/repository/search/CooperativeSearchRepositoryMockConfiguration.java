package verrimar.coopcycle.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link CooperativeSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CooperativeSearchRepositoryMockConfiguration {

    @MockBean
    private CooperativeSearchRepository mockCooperativeSearchRepository;
}
