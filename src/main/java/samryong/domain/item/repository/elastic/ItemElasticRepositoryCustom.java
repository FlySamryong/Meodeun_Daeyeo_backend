package samryong.domain.item.repository.elastic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import samryong.domain.item.document.ItemDocument;

public interface ItemElasticRepositoryCustom {

    Page<ItemDocument> searchItem(
            String keyword,
            String categoryName,
            String city,
            String district,
            String neighborhood,
            Pageable pageable);
}
