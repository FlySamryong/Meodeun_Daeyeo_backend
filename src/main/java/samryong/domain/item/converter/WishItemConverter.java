package samryong.domain.item.converter;

import java.util.List;
import java.util.stream.Collectors;
import samryong.domain.item.dto.ItemDTO.ItemPreviewResponseDTO;
import samryong.domain.item.dto.ItemDTO.WishListResponseDTO;
import samryong.domain.item.entity.Item;
import samryong.domain.item.entity.WishItem;
import samryong.domain.member.entity.Member;

public class WishItemConverter {

    public static WishItem toWishItem(Member member, Item item) {
        return WishItem.builder().member(member).item(item).build();
    }

    public static WishListResponseDTO toWishListResponseDTO(List<WishItem> wishList) {
        List<ItemPreviewResponseDTO> responseList =
                wishList.stream().map(ItemConverter::toItemPreviewResponseDTO).collect(Collectors.toList());

        return WishListResponseDTO.builder().itemPreviewResponseDTOList(responseList).build();
    }
}
