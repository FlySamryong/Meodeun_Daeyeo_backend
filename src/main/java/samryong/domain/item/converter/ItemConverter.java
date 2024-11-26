package samryong.domain.item.converter;

import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import samryong.domain.image.Image;
import samryong.domain.image.ImageConverter;
import samryong.domain.item.document.ItemDocument;
import samryong.domain.item.dto.ItemDTO;
import samryong.domain.item.dto.ItemDTO.ItemPreviewListResponseDTO;
import samryong.domain.item.dto.ItemDTO.ItemPreviewResponseDTO;
import samryong.domain.item.dto.ItemDTO.ItemRequestDTO;
import samryong.domain.item.dto.ItemDTO.ItemResponseDTO;
import samryong.domain.item.dto.ItemDTO.ItemResponseListDTO;
import samryong.domain.item.dto.ItemDTO.RecentItemResponseDTO;
import samryong.domain.item.dto.ItemDTO.RecentItemResponseListDTO;
import samryong.domain.item.entity.Category;
import samryong.domain.item.entity.Item;
import samryong.domain.item.entity.ItemCategory;
import samryong.domain.item.entity.WishItem;
import samryong.domain.location.converter.LocationConverter;
import samryong.domain.location.entity.Location;
import samryong.domain.member.converter.MemberConverter;
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

    public static ItemDocument toItemDocument(Item item, List<Category> categoryList) {
        return ItemDocument.builder()
                .id(item.getId())
                .name(item.getName())
                .createdDate(item.getCreatedAt().atOffset(ZoneOffset.UTC))
                .status(String.valueOf(item.getStatus()))
                .description(item.getDescription())
                .period(item.getPeriod())
                .fee(item.getFee())
                .deposit(item.getDeposit())
                .location(LocationConverter.toLocationDocument(item.getLocation()))
                .imageUrlList(
                        item.getImageList() != null
                                ? item.getImageList().stream().map(Image::getImageUri).collect(Collectors.toList())
                                : Collections.emptyList())
                .itemCategoryList(
                        categoryList.stream()
                                .map(CategoryConverter::toCategoryDocument)
                                .collect(Collectors.toList()))
                .build();
    }

    public static ItemPreviewResponseDTO toItemPreviewResponseDTO(ItemDocument itemDocument) {
        return ItemPreviewResponseDTO.builder()
                .id(itemDocument.getId())
                .name(itemDocument.getName())
                .status(itemDocument.getStatus())
                .fee(itemDocument.getFee())
                .createdDate(itemDocument.getCreatedDate().toString())
                .deposit(itemDocument.getDeposit())
                .imageUrl(
                        itemDocument.getImageUrlList() != null && !itemDocument.getImageUrlList().isEmpty()
                                ? itemDocument.getImageUrlList().get(0)
                                : null)
                .location(LocationConverter.toLocationResponseDTO(itemDocument.getLocation()))
                .build();
    }

    public static ItemPreviewResponseDTO toItemPreviewResponseDTO(WishItem wishItem) {
        Item item = wishItem.getItem();
        return ItemPreviewResponseDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .status(item.getStatus() != null ? item.getStatus().toString() : null)
                .fee(item.getFee())
                .createdDate(item.getCreatedAt() != null ? item.getCreatedAt().toString() : null)
                .deposit(item.getDeposit())
                .imageUrl(
                        item.getImageList() != null && !item.getImageList().isEmpty()
                                ? item.getImageList().get(0).getImageUri()
                                : null)
                .location(
                        item.getLocation() != null
                                ? LocationConverter.toLocationResponseDTO(item.getLocation())
                                : null)
                .build();
    }

    public static ItemPreviewListResponseDTO toItemPreviewResponseListDTO(
            Page<ItemDocument> itemDocumentList) {
        return ItemPreviewListResponseDTO.builder()
                .totalPage(itemDocumentList.getTotalPages())
                .currentElement(itemDocumentList.getNumberOfElements())
                .totalElement(itemDocumentList.getTotalElements())
                .itemPreviewResponseDTOList(
                        itemDocumentList.getContent().stream()
                                .map(ItemConverter::toItemPreviewResponseDTO)
                                .collect(Collectors.toList()))
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
                .location(LocationConverter.toLocationResponseDTO(item.getLocation()))
                .owner(MemberConverter.toOwnerResponseDTO(item.getMember()))
                .imageList(
                        item.getImageList() != null
                                ? item.getImageList().stream()
                                        .map(ImageConverter::toImageResponseDTO)
                                        .collect(Collectors.toList())
                                : Collections.emptyList())
                .build();
    }

    public static RecentItemResponseDTO toRecentItemResponseDTO(Item item) {
        return ItemDTO.RecentItemResponseDTO.builder()
                .itemId(item.getId())
                .name(item.getName())
                .location(LocationConverter.toLocationResponseDTO(item.getLocation()))
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
