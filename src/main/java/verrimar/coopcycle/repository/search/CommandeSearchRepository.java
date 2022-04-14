package verrimar.coopcycle.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.stream.Stream;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import verrimar.coopcycle.domain.Commande;

/**
 * Spring Data Elasticsearch repository for the {@link Commande} entity.
 */
public interface CommandeSearchRepository extends ElasticsearchRepository<Commande, Long>, CommandeSearchRepositoryInternal {}

interface CommandeSearchRepositoryInternal {
    Stream<Commande> search(String query);
}

class CommandeSearchRepositoryInternalImpl implements CommandeSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    CommandeSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<Commande> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, Commande.class).map(SearchHit::getContent).stream();
    }
}
