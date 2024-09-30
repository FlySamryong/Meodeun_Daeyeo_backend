package samryong.domain.location.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class LocationDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LocationRequestDTO {

        @NotBlank(message = "주소는 필수 입력입니다.")
        private String city;

        @NotBlank(message = "주소는 필수 입력입니다.")
        private String district;

        @NotBlank(message = "주소는 필수 입력입니다.")
        private String neighborhood;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LocationResponseDTO {

        private String city;
        private String district;
        private String neighborhood;
    }
}
