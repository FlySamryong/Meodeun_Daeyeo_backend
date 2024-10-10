package samryong.domain.location.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import samryong.domain.location.document.LocationDocument;

@Repository
public interface LocationElasticRepository
        extends ElasticsearchRepository<LocationDocument, Long> {}
