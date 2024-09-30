package samryong.domain.item.converter;

import org.springframework.stereotype.Component;
import samryong.domain.item.document.CategoryDocument;
import samryong.domain.item.dto.CategoryDTO.CategoryRequestDTO;
import samryong.domain.item.entity.Category;

@Component
public class CategoryConverter {

    public static Category toCategory(CategoryRequestDTO requestDTO) {
        return Category.builder().name(requestDTO.getName()).build();
    }

    public static CategoryDocument toCategoryDocument(Category category) {
        return CategoryDocument.builder().id(category.getId()).name(category.getName()).build();
    }
}
