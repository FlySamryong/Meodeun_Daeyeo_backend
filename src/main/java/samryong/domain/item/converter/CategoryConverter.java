package samryong.domain.item.converter;

import org.springframework.stereotype.Component;
import samryong.domain.item.dto.CategoryDTO.CategoryRequestDTO;
import samryong.domain.item.entity.Category;

@Component
public class CategoryConverter {

    public static Category toCategory(CategoryRequestDTO requestDTO) {
        return Category.builder().name(requestDTO.getName()).build();
    }
}
