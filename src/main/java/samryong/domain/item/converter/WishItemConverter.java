package samryong.domain.item.converter;

import java.util.List;
import java.util.stream.Collectors;
import samryong.domain.item.dto.ItemDTO.ItemPreviewResponseDTO;
import samryong.domain.item.dto.ItemDTO.WishListResponseDTO;
import samryong.domain.item.entity.Item;
import samryong.domain.item.entity.WishItem;
import samryong.domain.location.converter.LocationConverter;
import samryong.domain.member.entity.Member;

public class WishItemConverter {
    public static WishItem toWishItem(Member member, Item item) {
        return WishItem.builder().member(member).item(item).build();
    }

    public static Item WishItmetoItem(WishItem wishItem) {
        return wishItem.getItem();
    }

    public static List<Item> ItemListtoWishList(List<WishItem> wishlist) {
        return wishlist.stream()
                .map(WishItemConverter::WishItmetoItem) // WishItem을 Item으로 변환
                .collect(Collectors.toList()); // 변환된 Item들을 리스트로 수집
    }

    public static WishListResponseDTO toWishListResponse(List<WishItem> wishList) {
        List<ItemPreviewResponseDTO> responseList =
                wishList.stream()
                        .map(
                                wishItem -> {
                                    Item item = wishItem.getItem(); // WishItem에서 Item 가져오기
                                    return ItemPreviewResponseDTO.builder()
                                            .id(item.getId())
                                            .name(item.getName())
                                            .status(item.getStatus() != null ? item.getStatus().toString() : null)
                                            .fee(item.getFee())
                                            .createdDate(
                                                    item.getCreatedAt() != null ? item.getCreatedAt().toString() : null)
                                            .deposit(item.getDeposit())
                                            .imageUrl(
                                                    (item.getImageList() != null && !item.getImageList().isEmpty())
                                                            ? item.getImageList().get(0).getImageUri()
                                                            : null)
                                            .location(
                                                    item.getLocation() != null
                                                            ? LocationConverter.toLocationResponseDTO(item.getLocation())
                                                            : null)
                                            .build();
                                })
                        .collect(Collectors.toList());

        // 빌더를 사용해 WishListResponseDTO 생성
        return WishListResponseDTO.builder().WishListResponseDTOList(responseList).build();
    }
}
