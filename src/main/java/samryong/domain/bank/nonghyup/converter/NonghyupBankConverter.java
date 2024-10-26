package samryong.domain.bank.nonghyup.converter;

import org.springframework.stereotype.Component;
import samryong.domain.bank.nonghyup.dto.request.BankTransferRequestDTO.DepositRequestDTO;
import samryong.domain.bank.nonghyup.dto.request.BankTransferRequestDTO.WithdrawRequestDTO;

@Component
public class NonghyupBankConverter {

    public static WithdrawRequestDTO toWithdrawRequestDTO(
            String finTechAccountNum, Long amount, String withdrawOtlt) {
        return new WithdrawRequestDTO(finTechAccountNum, amount.toString(), withdrawOtlt);
    }

    public static DepositRequestDTO toDepositRequestDTO(
            String accountNum, Long amount, String depositOtlt) {
        return new DepositRequestDTO(accountNum, amount.toString(), depositOtlt, null);
    }
}
