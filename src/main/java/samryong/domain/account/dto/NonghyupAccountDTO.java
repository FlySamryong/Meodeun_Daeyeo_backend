package samryong.domain.account.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class NonghyupAccountDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NonghyupAccountRequestDTO {

        @NotBlank(message = "계좌번호는 필수 입력값입니다.")
        private String accountNumber;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NonghyupAccountResponseDTO {
        private String accountNum;
        private String finTechAccountNum;
    }
}
