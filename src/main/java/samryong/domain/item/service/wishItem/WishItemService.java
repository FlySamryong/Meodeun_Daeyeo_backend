package samryong.domain.item.service.wishItem;

import samryong.domain.item.dto.ItemDTO.WishListResponseDTO;
import samryong.domain.member.entity.Member;

public interface WishItemService {
    WishListResponseDTO getWishList(Member member);

    void addWishItem(Member member, Long itemId);
}
