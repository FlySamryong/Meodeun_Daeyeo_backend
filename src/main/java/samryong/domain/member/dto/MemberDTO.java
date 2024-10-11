package samryong.domain.member.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import samryong.domain.account.dto.NonghyupAccountDTO.NonghyupAccountResponseDTO;
import samryong.domain.location.dto.LocationDTO.LocationResponseDTO;

public class MemberDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyInformationResponseDTO {

        private String nickName;

        private String email;

        private String profileImage;

        private double mannerRate;

        private LocationResponseDTO location;

        private List<NonghyupAccountResponseDTO> accountList;
    }
}
