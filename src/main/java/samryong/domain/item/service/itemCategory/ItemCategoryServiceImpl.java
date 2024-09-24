package samryong.domain.item.service.itemCategory;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import samryong.domain.item.converter.ItemCategoryConverter;
import samryong.domain.item.entity.Category;
import samryong.domain.item.entity.Item;
import samryong.domain.item.entity.ItemCategory;

@Service
@RequiredArgsConstructor
public class ItemCategoryServiceImpl implements ItemCategoryService {

    public List<ItemCategory> createItemCategoryList(Item item, List<Category> categoryList) {
        return categoryList.stream().map(category -> createItemCategory(item, category)).toList();
    }

    public ItemCategory createItemCategory(Item item, Category category) {
        ItemCategory itemCategory = ItemCategoryConverter.toItemCategory(item, category);
        item.addItemCategory(itemCategory);
        category.addItemCategory(itemCategory);
        return itemCategory;
    }
}
