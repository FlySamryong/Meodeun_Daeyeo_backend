package samryong.domain.item.repository.elastic;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import samryong.domain.item.document.ItemDocument;

@Repository
public interface ItemElasticRepository
        extends ElasticsearchRepository<ItemDocument, Long>, ItemElasticRepositoryCustom {}
