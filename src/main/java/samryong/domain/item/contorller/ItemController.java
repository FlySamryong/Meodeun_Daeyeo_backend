package samryong.domain.item.contorller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import samryong.domain.item.converter.ItemConverter;
import samryong.domain.item.dto.CategoryDTO.CategoryRequestDTO;
import samryong.domain.item.dto.ItemDTO;
import samryong.domain.item.dto.ItemDTO.ItemRequestDTO;
import samryong.domain.item.entity.Item;
import samryong.domain.item.service.category.CategoryService;
import samryong.domain.item.service.item.ItemService;
import samryong.domain.location.dto.LocationDTO.LocationRequestDTO;
import samryong.domain.member.entity.Member;
import samryong.global.response.ApiResponse;
import samryong.security.resolver.annotation.AuthMember;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/item")
@Tag(name = "Item", description = "대여 물품(Item) 관련 API")
public class ItemController {

    private final ItemService itemService;
    private final CategoryService categoryService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "물품 등록", description = "사용자가 물품을 등록합니다.")
    public ApiResponse<Long> createItem(
            @AuthMember Member member,
            @RequestPart(value = "imageList", required = false) List<MultipartFile> imageList,
            @RequestPart(value = "itemDTO") @Valid ItemRequestDTO itemDTO,
            @RequestPart(value = "locationDTO") @Valid LocationRequestDTO locationDTO) {
        return ApiResponse.onSuccess(
                "물품 등록 성공", itemService.createItem(member, itemDTO, locationDTO, imageList));
    }

    @PostMapping("/create/category")
    @Operation(summary = "관리자의 카테고리 등록 API", description = "카테고리를 등록합니다.")
    public ApiResponse<Long> createCategory(@RequestBody @Valid CategoryRequestDTO categoryDTO) {
        return ApiResponse.onSuccess("카테고리 등록 성공", categoryService.createCategory(categoryDTO));
    }

    @GetMapping("/{itemId}")
    @Operation(summary = "아이템 상세 조회", description = "사용자가 물품을 조회합니다.")
    public ApiResponse<ItemDTO.ItemResponseDTO> getItemDetails(@PathVariable Long itemId) {
        ItemDTO.ItemResponseDTO itemResponse =
                ItemConverter.toResponseDTO(itemService.showItem(itemId));
        return ApiResponse.onSuccess("물품조회 완료", itemResponse);
    }

    @GetMapping("/recent")
    @Operation(summary = "아이템 상세 조회", description = "사용자가 물품을 조회합니다.")
    public ApiResponse<List<ItemDTO.ItemResponseDTO>> getItemDetails(
            @ModelAttribute("recentItems") List<Object> recentItems) {
        List<ItemDTO.ItemResponseDTO> itemResponseList =
                recentItems.stream()
                        .map(
                                item -> {
                                    Item recentItem = (Item) item; // RecentItem은 예시
                                    Long itemId = recentItem.getId(); // 아이템 ID 가져오기
                                    return ItemConverter.toResponseDTO(
                                            itemService.showItem(itemId)); // 아이템을 조회하여 DTO로 변환
                                })
                        .collect(Collectors.toList()); // 리스트로 수집

        return ApiResponse.onSuccess("최근 물품 조회 완료", itemResponseList);
    }
}
