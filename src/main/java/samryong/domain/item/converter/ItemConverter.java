package samryong.domain.item.converter;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import samryong.domain.item.dto.ItemDTO;
import samryong.domain.item.dto.ItemDTO.ItemRequestDTO;
import samryong.domain.item.entity.Item;
import samryong.domain.location.entity.Location;
import samryong.domain.member.entity.Member;

@Component
public class ItemConverter {

    public static Item toItem(ItemRequestDTO requestDTO, Location location, Member member) {
        return Item.builder()
                .name(requestDTO.getName())
                .description(requestDTO.getDescription())
                .period(requestDTO.getPeriod())
                .fee(requestDTO.getFee())
                .deposit(requestDTO.getDeposit())
                .location(location)
                .member(member)
                .build();
    }

    public static ItemDTO.ItemResponseDTO toResponseDTO(Item item) {
        return ItemDTO.ItemResponseDTO.builder()
                .itemId(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .period(item.getPeriod())
                .fee(item.getFee())
                .deposit(item.getDeposit())
                .categoryList(item.getItemCategoryList())
                .build();
    }

    public static List<ItemDTO.ItemResponseDTO> toResponseDTOList(List<Object> objectItemList) {
        return objectItemList.stream()
                .map(
                        item -> {
                            Item castedItem = (Item) item;
                            return toResponseDTO(castedItem);
                        })
                .collect(Collectors.toList());
    }
}
