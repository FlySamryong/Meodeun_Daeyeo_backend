package samryong.domain.bank.nonghyup.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BankTransferRequestDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DepositRequestDTO { // 입금 이체 API 요청 DTO

        private String accountNum; // 계좌번호
        private String transferAmount; // 거래금액
        private String depositOtlt; // 입금계좌인자내용
        private String withdrawOtlt; // 출금계좌인자내용
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WithdrawRequestDTO { // 출금 이체 API 요청 DTO

        private String finAcno; // 핀-어카운트번호
        private String transferAmount; // 거래금액
        private String withdrawOtlt; // 출금계좌인자내용
    }
}
