package samryong.domain.item.service.wishItem;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import samryong.domain.item.converter.WishItemConverter;
import samryong.domain.item.dto.ItemDTO.WishListResponseDTO;
import samryong.domain.item.entity.Item;
import samryong.domain.item.entity.WishItem;
import samryong.domain.item.repository.ItemRepository;
import samryong.domain.member.entity.Member;
import samryong.global.code.GlobalErrorCode;
import samryong.global.exception.GlobalException;

@Service
@RequiredArgsConstructor
public class WishItemServiceImpl implements WishItemService {

    private final ItemRepository itemRepository;

    @Override
    public WishListResponseDTO getWishList(Member member) {
        return WishItemConverter.toWishListResponse(member.getWishList());
    }

    @Override
    public void addWishItem(Member member, Long itemId) {
        Item item =
                itemRepository
                        .findById(itemId)
                        .orElseThrow(() -> new GlobalException(GlobalErrorCode.ITEM_NOT_FOUND));
        WishItem wishItem = WishItemConverter.toWishItem(member, item);
        member.addWishList(wishItem);
    }
}
