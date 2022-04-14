package verrimar.coopcycle.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.stream.Stream;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import verrimar.coopcycle.domain.Livreur;

/**
 * Spring Data Elasticsearch repository for the {@link Livreur} entity.
 */
public interface LivreurSearchRepository extends ElasticsearchRepository<Livreur, Long>, LivreurSearchRepositoryInternal {}

interface LivreurSearchRepositoryInternal {
    Stream<Livreur> search(String query);
}

class LivreurSearchRepositoryInternalImpl implements LivreurSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    LivreurSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<Livreur> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, Livreur.class).map(SearchHit::getContent).stream();
    }
}
