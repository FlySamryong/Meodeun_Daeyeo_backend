package samryong.domain.item.converter;

import org.springframework.stereotype.Component;
import samryong.domain.item.entity.Category;
import samryong.domain.item.entity.Item;
import samryong.domain.item.entity.ItemCategory;

@Component
public class ItemCategoryConverter {

    public static ItemCategory toItemCategory(Item item, Category category) {
        return ItemCategory.builder().item(item).category(category).build();
    }
}
