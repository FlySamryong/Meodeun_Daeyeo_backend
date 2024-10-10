package samryong.domain.item.converter;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import samryong.domain.item.dto.ItemDTO;
import samryong.domain.item.dto.ItemDTO.ItemRequestDTO;
import samryong.domain.item.dto.ItemDTO.ItemResponseDTO;
import samryong.domain.item.dto.ItemDTO.ItemResponseListDTO;
import samryong.domain.item.dto.ItemDTO.RecentItemResponseDTO;
import samryong.domain.item.dto.ItemDTO.RecentItemResponseListDTO;
import samryong.domain.item.entity.Item;
import samryong.domain.item.entity.ItemCategory;
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

    public static ItemResponseDTO toItemResponseDTO(Item item) {
        return ItemResponseDTO.builder()
                .itemId(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .period(item.getPeriod())
                .fee(item.getFee())
                .deposit(item.getDeposit())
                .categoryList(
                        CategoryConverter.toCategoryResponseListDTO(
                                item.getItemCategoryList().stream()
                                        .map(ItemCategory::getCategory)
                                        .collect(Collectors.toList())))
                .build();
    }

    public static RecentItemResponseDTO toRecentItemResponseDTO(Item item) {
        return ItemDTO.RecentItemResponseDTO.builder()
                .name(item.getName())
                .description(item.getDescription())
                .fee(item.getFee())
                .categoryList(
                        item.getItemCategoryList().stream()
                                .map(ItemCategory::getCategory)
                                .map(CategoryConverter::toCategoryResponseDTO)
                                .collect(Collectors.toList()))
                .build();
    }

    public static ItemResponseListDTO toItemResponseListDTO(List<Item> itemList) {
        return ItemResponseListDTO.builder()
                .itemList(
                        itemList.stream().map(ItemConverter::toItemResponseDTO).collect(Collectors.toList()))
                .build();
    }

    public static RecentItemResponseListDTO toRecentItemResponseListDTO(List<Item> itemList) {
        return RecentItemResponseListDTO.builder()
                .itemList(
                        itemList.stream()
                                .map(ItemConverter::toRecentItemResponseDTO)
                                .collect(Collectors.toList()))
                .build();
    }
}
