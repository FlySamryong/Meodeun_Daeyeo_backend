package samryong.domain.item.service.category;

import java.util.List;
import samryong.domain.item.dto.CategoryDTO.CategoryRequestDTO;
import samryong.domain.item.entity.Category;

public interface CategoryService {

    List<Category> getCategoryList(List<CategoryRequestDTO> categoryList);

    Category getCategoryByName(String name);

    Long createCategory(CategoryRequestDTO categoryDTO);
}
