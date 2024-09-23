package samryong.domain.item.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ItemRegistrationDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemRequestDTO {
        @NotNull(message = "상품 이름은 필수 입력입니다.")
        private String name;

        @NotNull(message = "상품 설명은 필수 입력입니다.")
        private String description;

        @NotNull(message = "대여 가능 기능은 필수 입력입니다.")
        private Long period;

        @NotNull(message = "대여비는 필수 입력입니다.")
        private Long fee;

        private Long deposit;

        private String imageUrl;

        @NotNull(message = "주소지는 필수 입력입니다.")
        private Long locationId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemResponseDTO {
        private Long id;
        private String name;
        private String description;
        private Long period;
        private Long fee;
        private Long deposit;
        private String imageUrl;
        private String status;
        private String location;
    }
}
