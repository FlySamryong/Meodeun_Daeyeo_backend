package samryong.domain.item.service.category;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import samryong.domain.item.converter.CategoryConverter;
import samryong.domain.item.dto.CategoryDTO.CategoryRequestDTO;
import samryong.domain.item.entity.Category;
import samryong.domain.item.repository.CategoryRepository;
import samryong.global.code.GlobalErrorCode;
import samryong.global.exception.GlobalException;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getCategoryList(List<CategoryRequestDTO> categoryList) {
        return categoryList.stream()
                .map(CategoryRequestDTO::getName)
                .map(this::getCategoryByName)
                .toList();
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository
                .findByName(name)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.CATEGORY_NOT_FOUND));
    }

    @Override
    public Long createCategory(CategoryRequestDTO categoryDTO) {
        Category category = CategoryConverter.toCategory(categoryDTO);
        categoryRepository.save(category);
        return category.getId();
    }
}
