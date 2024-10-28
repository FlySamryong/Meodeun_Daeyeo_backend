package samryong.domain.image;

import org.springframework.stereotype.Component;
import samryong.domain.image.ImageDTO.ImageResponseDTO;
import samryong.domain.item.entity.Item;
import samryong.domain.member.entity.Member;

@Component
public class ImageConverter {

    public static Image toItemImage(String imageUri, Item item) {
        return Image.builder().imageUri(imageUri).item(item).build();
    }

    public static Image toProfileImage(String imageUri, Member member) {
        return Image.builder().imageUri(imageUri).member(member).build();
    }

    public static ImageResponseDTO toImageResponseDTO(Image image) {
        return ImageResponseDTO.builder().imageUri(image.getImageUri()).build();
    }
}
