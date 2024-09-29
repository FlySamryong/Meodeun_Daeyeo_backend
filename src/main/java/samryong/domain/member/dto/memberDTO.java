package samryong.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import samryong.domain.account.dto.NonghyupAccountDTO;
import samryong.domain.image.Image;
import samryong.domain.location.dto.LocationDTO;

public class memberDTO {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberRequestDTO {
        @NotBlank(message = "닉네임은 필수 입력 사항입니다.")
        private String nickName;

        @NotBlank(message = "이메일은 필수 입력 사항입니다.")
        private String email;

        private Image profileImage;

        @NotBlank(message = "매너온도는 필수 입력 사항입니다.")
        private Long mannerRate;

        @NotBlank(message = "주소지는 필수 입력 사항입니다.")
        private LocationDTO.LocationRequestDTO Location;

        @NotEmpty(message = "계좌번호는 필수 입력 사항입니다.")
        private List<NonghyupAccountDTO.NonghyupAccountRequestDTO> Account;
    }
}
