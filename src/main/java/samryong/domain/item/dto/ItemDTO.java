package samryong.domain.item.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import samryong.domain.item.dto.CategoryDTO.CategoryRequestDTO;
import samryong.domain.location.dto.LocationDTO.LocationRequestDTO;
import samryong.domain.location.dto.LocationDTO.LocationResponseDTO;

public class ItemDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemRequestDTO {

        @NotBlank(message = "상품 이름은 필수 입력입니다.")
        private String name;

        @NotBlank(message = "상품 설명은 필수 입력입니다.")
        @Size(max = 500, message = "상품 설명은 500자 이내여야 합니다.")
        private String description;

        @NotNull(message = "대여 가능 기간은 필수 입력입니다.")
        @Min(value = 1, message = "대여 가능 기간은 0보다 커야 합니다.")
        private Long period;

        @NotNull(message = "대여비는 필수 입력입니다.")
        @Min(value = 1, message = "대여비는 0보다 커야 합니다.")
        private Long fee;

        @NotNull(message = "보증금은 필수 입력입니다.")
        @Min(value = 1, message = "보증금은 0보다 커야 합니다.")
        private Long deposit;

        private ArrayList<CategoryRequestDTO> categoryList;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemListRequestDTO {

        private String keyword;

        private CategoryRequestDTO category;

        private LocationRequestDTO location;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemPreviewResponseDTO {

        // 아이템 전체 조회 시에 대한 미리 보기 응답
        private Long id;

        private String name;

        private String status;

        private String createdDate;

        private Long fee;

        private Long deposit;

        private String imageUrl; // 이미지 목록 중 첫번째 값

        private LocationResponseDTO location;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemPreviewListResponseDTO {

        private int currentElement; // 현재 페이지 아이템 개수
        private int totalPage; // 전체 페이지
        private long totalElement; // 전체 아이템 개수
        private List<ItemPreviewResponseDTO> itemPreviewResponseDTOList;
    }
}
