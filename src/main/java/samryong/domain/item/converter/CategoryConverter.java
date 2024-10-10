package samryong.domain.item.converter;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import samryong.domain.item.document.CategoryDocument;
import samryong.domain.item.dto.CategoryDTO.CategoryRequestDTO;
import samryong.domain.item.dto.CategoryDTO.CategoryResponseDTO;
import samryong.domain.item.entity.Category;

@Component
public class CategoryConverter {

    public static Category toCategory(CategoryRequestDTO requestDTO) {
        return Category.builder().name(requestDTO.getName()).build();
    }

    public static CategoryResponseDTO toCategoryResponseDTO(Category category) {
        return CategoryResponseDTO.builder().name(category.getName()).build();
    }

    public static List<CategoryResponseDTO> toCategoryResponseListDTO(List<Category> categoryList) {
        return categoryList.stream()
                .map(CategoryConverter::toCategoryResponseDTO)
                .collect(Collectors.toList());

    public static CategoryDocument toCategoryDocument(Category category) {
        return CategoryDocument.builder().id(category.getId()).name(category.getName()).build();

    }
}
