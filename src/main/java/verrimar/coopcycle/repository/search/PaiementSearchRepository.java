package verrimar.coopcycle.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.stream.Stream;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import verrimar.coopcycle.domain.Paiement;

/**
 * Spring Data Elasticsearch repository for the {@link Paiement} entity.
 */
public interface PaiementSearchRepository extends ElasticsearchRepository<Paiement, Long>, PaiementSearchRepositoryInternal {}

interface PaiementSearchRepositoryInternal {
    Stream<Paiement> search(String query);
}

class PaiementSearchRepositoryInternalImpl implements PaiementSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    PaiementSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<Paiement> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, Paiement.class).map(SearchHit::getContent).stream();
    }
}
