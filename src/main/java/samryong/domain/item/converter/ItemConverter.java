package samryong.domain.item.converter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import samryong.domain.image.Image;
import samryong.domain.item.document.ItemDocument;
import samryong.domain.item.dto.ItemDTO.ItemPreviewListResponseDTO;
import samryong.domain.item.dto.ItemDTO.ItemPreviewResponseDTO;
import samryong.domain.item.dto.ItemDTO.ItemRequestDTO;
import samryong.domain.item.entity.Category;
import samryong.domain.item.entity.Item;
import samryong.domain.location.converter.LocationConverter;
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

    public static ItemDocument toItemDocument(Item item, List<Category> categoryList) {
        return ItemDocument.builder()
                .id(item.getId())
                .name(item.getName())
                .createdDate(item.getCreatedAt().toString())
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
                .createdDate(itemDocument.getCreatedDate())
                .deposit(itemDocument.getDeposit())
                .imageUrl(
                        itemDocument.getImageUrlList() != null && !itemDocument.getImageUrlList().isEmpty()
                                ? itemDocument.getImageUrlList().get(0)
                                : null)
                .location(LocationConverter.toLocationResponseDTO(itemDocument.getLocation()))
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
}
