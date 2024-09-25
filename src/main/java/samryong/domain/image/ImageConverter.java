package samryong.domain.image;

import org.springframework.stereotype.Component;
import samryong.domain.item.entity.Item;

@Component
public class ImageConverter {

    public static Image toItemImage(String imageUri, Item item) {
        return Image.builder().imageUri(imageUri).item(item).build();
    }
}
