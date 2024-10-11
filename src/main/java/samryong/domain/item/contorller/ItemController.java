package samryong.domain.item.contorller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import samryong.domain.item.dto.CategoryDTO.CategoryRequestDTO;
import samryong.domain.item.dto.ItemDTO.ItemListRequestDTO;
import samryong.domain.item.dto.ItemDTO.ItemPreviewListResponseDTO;
import samryong.domain.item.dto.ItemDTO.ItemRequestDTO;
import samryong.domain.item.dto.ItemDTO.ItemResponseDTO;
import samryong.domain.item.dto.ItemDTO.RecentItemResponseListDTO;
import samryong.domain.item.service.category.CategoryService;
import samryong.domain.item.service.item.ItemService;
import samryong.domain.location.dto.LocationDTO.LocationRequestDTO;
import samryong.domain.member.entity.Member;
import samryong.domain.redis.service.RecentItemService;
import samryong.global.annotation.AuthMember;
import samryong.global.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/item")
@Tag(name = "Item", description = "대여 물품(Item) 관련 API")
public class ItemController {

    private final ItemService itemService;
    private final CategoryService categoryService;
    private final RecentItemService recentItemService;

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
    @Operation(summary = "물품 상세 조회", description = "사용자가 물품을 상세 조회합니다.")
    public ApiResponse<ItemResponseDTO> getItemDetails(
            @PathVariable Long itemId, @AuthMember Member member) {
        return ApiResponse.onSuccess("물품조회 완료", itemService.getItemDetail(itemId, member));
    }

    @GetMapping("/recent")
    @Operation(summary = "최근 물품 조회", description = "사용자가 최근에 본 아이템을 조회합니다.")
    public ApiResponse<RecentItemResponseListDTO> getRecentItem(@AuthMember Member member) {
        return ApiResponse.onSuccess("최근 물품 조회 완료", recentItemService.getRecentItemList(member));
    }

    @PostMapping("/search")
    @Operation(summary = "물품 검색", description = "물품명, 물품 설명, 카테고리, 위치를 통해 물품을 검색합니다.")
    public ApiResponse<ItemPreviewListResponseDTO> searchItem(
            @RequestBody @Valid ItemListRequestDTO requestDTO,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page) {
        return ApiResponse.onSuccess("물품 검색 성공", itemService.searchItem(requestDTO, page));
    }
}
