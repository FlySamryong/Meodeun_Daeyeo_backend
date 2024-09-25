package samryong.domain.item.service.itemCategory;

import java.util.List;
import samryong.domain.item.entity.Category;
import samryong.domain.item.entity.Item;
import samryong.domain.item.entity.ItemCategory;

public interface ItemCategoryService {

    List<ItemCategory> createItemCategoryList(Item item, List<Category> categoryList);

    ItemCategory createItemCategory(Item item, Category category);
}
