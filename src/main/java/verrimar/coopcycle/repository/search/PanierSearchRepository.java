package verrimar.coopcycle.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.stream.Stream;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import verrimar.coopcycle.domain.Panier;

/**
 * Spring Data Elasticsearch repository for the {@link Panier} entity.
 */
public interface PanierSearchRepository extends ElasticsearchRepository<Panier, Long>, PanierSearchRepositoryInternal {}

interface PanierSearchRepositoryInternal {
    Stream<Panier> search(String query);
}

class PanierSearchRepositoryInternalImpl implements PanierSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    PanierSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<Panier> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, Panier.class).map(SearchHit::getContent).stream();
    }
}
