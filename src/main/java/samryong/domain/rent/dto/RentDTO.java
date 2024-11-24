package samryong.domain.rent.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import samryong.domain.location.dto.LocationDTO.LocationResponseDTO;

public class RentDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyRentOrLoanResponseListDTO {
        List<RentResponseDTO> rentOrLoanList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RentResponseDTO {

        private Long id;

        private String itemName; // 물품 이름

        private String period; // 대여 기간

        private LocationResponseDTO location; // 물품 거래 장소

        private String status; // 대여 상태
    }
}
