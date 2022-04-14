package verrimar.coopcycle.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.stream.Stream;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import verrimar.coopcycle.domain.Commercant;

/**
 * Spring Data Elasticsearch repository for the {@link Commercant} entity.
 */
public interface CommercantSearchRepository extends ElasticsearchRepository<Commercant, Long>, CommercantSearchRepositoryInternal {}

interface CommercantSearchRepositoryInternal {
    Stream<Commercant> search(String query);
}

class CommercantSearchRepositoryInternalImpl implements CommercantSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    CommercantSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<Commercant> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, Commercant.class).map(SearchHit::getContent).stream();
    }
}
