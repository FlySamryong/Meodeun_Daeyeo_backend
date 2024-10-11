package samryong.domain.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CategoryDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategoryRequestDTO {

        @NotBlank(message = "카테고리 이름은 필수 입력입니다.")
        private String name;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CategoryResponseDTO {
        private String name;
    }
}
