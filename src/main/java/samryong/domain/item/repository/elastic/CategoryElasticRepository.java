package samryong.domain.item.repository.elastic;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import samryong.domain.item.document.CategoryDocument;

@Repository
public interface CategoryElasticRepository
        extends ElasticsearchRepository<CategoryDocument, Long> {}
