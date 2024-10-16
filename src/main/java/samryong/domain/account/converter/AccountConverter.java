package samryong.domain.account.converter;

import java.util.List;
import org.springframework.stereotype.Component;
import samryong.domain.account.dto.NonghyupAccountDTO.NonghyupAccountResponseDTO;
import samryong.domain.account.entity.Account;

@Component
public class AccountConverter {

    public static Account toAccount(String accountNum, String finTechAccountNum) {
        return Account.builder().accountNum(accountNum).finTechAccountNum(finTechAccountNum).build();
    }

    public static NonghyupAccountResponseDTO toAccountResponseDTO(Account account) {

        return NonghyupAccountResponseDTO.builder()
                .accountNum(account.getAccountNum())
                .finTechAccountNum(account.getFinTechAccountNum())
                .build();
    }

    public static List<NonghyupAccountResponseDTO> toAccountResponseListDTO(
            List<Account> accountList) {
        return accountList.stream().map(AccountConverter::toAccountResponseDTO).toList();
    }
}
