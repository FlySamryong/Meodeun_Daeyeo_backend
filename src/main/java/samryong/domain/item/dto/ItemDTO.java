package samryong.domain.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import samryong.domain.item.entity.ItemCategory;

public class ItemDTO {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemRequestDTO {

        @NotBlank(message = "상품 이름은 필수 입력입니다.")
        private String name;

        @NotBlank(message = "상품 설명은 필수 입력입니다.")
        private String description;

        @NotNull(message = "대여 가능 기간은 필수 입력입니다.")
        private Long period;

        @NotNull(message = "대여비는 필수 입력입니다.")
        private Long fee;

        @NotNull(message = "보증금은 필수 입력입니다.")
        private Long deposit;

        private ArrayList<CategoryDTO.CategoryRequestDTO> categoryList;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemResponseDTO {
        private Long itemId;
        private String name;
        private String description;
        private Long period;
        private Long fee;
        private Long deposit;
        private List<ItemCategory> categoryList;
    }

}
