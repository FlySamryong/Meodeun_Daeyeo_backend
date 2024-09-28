package samryong.domain.item.repository.elastic;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Repository;
import samryong.domain.item.document.ItemDocument;

@Repository
@RequiredArgsConstructor
public class ItemElasticRepositoryImpl implements ItemElasticRepositoryCustom {

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public Page<ItemDocument> searchItem(
            String keyword,
            String categoryName,
            String city,
            String district,
            String neighborhood,
            Pageable pageable) {

        Criteria criteria = buildCriteria(keyword, categoryName, city, district, neighborhood);
        Sort sort = Sort.by(Sort.Order.desc("createdDate")); // 내림차순 정렬 (최신순)

        CriteriaQuery query = new CriteriaQuery(criteria).addSort(sort).setPageable(pageable);

        SearchHits<ItemDocument> searchHits = elasticsearchOperations.search(query, ItemDocument.class);
        List<ItemDocument> itemDocumentList =
                searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());

        return new PageImpl<>(itemDocumentList, pageable, searchHits.getTotalHits());
    }

    private Criteria buildCriteria(
            String keyword, String categoryName, String city, String district, String neighborhood) {

        Criteria criteria = new Criteria();

        if (isNotEmpty(keyword)) {
            for (String key : keyword.split("\\s+")) {
                criteria = criteria.or("name").contains(key).or("description").contains(key);
            }
        }

        if (isNotEmpty(categoryName)) criteria = criteria.and("itemCategoryList.name").is(categoryName);
        if (isNotEmpty(city)) criteria = criteria.and("location.city").is(city);
        if (isNotEmpty(district)) criteria = criteria.and("location.district").is(district);
        if (isNotEmpty(neighborhood)) criteria = criteria.and("location.neighborhood").is(neighborhood);

        return criteria;
    }

    private boolean isNotEmpty(String value) {
        return value != null && !value.isEmpty();
    }
}
