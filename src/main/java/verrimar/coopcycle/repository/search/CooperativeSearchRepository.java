package verrimar.coopcycle.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.stream.Stream;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import verrimar.coopcycle.domain.Cooperative;

/**
 * Spring Data Elasticsearch repository for the {@link Cooperative} entity.
 */
public interface CooperativeSearchRepository extends ElasticsearchRepository<Cooperative, Long>, CooperativeSearchRepositoryInternal {}

interface CooperativeSearchRepositoryInternal {
    Stream<Cooperative> search(String query);
}

class CooperativeSearchRepositoryInternalImpl implements CooperativeSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    CooperativeSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<Cooperative> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, Cooperative.class).map(SearchHit::getContent).stream();
    }
}
