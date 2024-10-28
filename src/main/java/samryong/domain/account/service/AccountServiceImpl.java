package samryong.domain.account.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import samryong.domain.account.entity.Account;
import samryong.domain.bank.nonghyup.converter.NonghyupBankConverter;
import samryong.domain.bank.nonghyup.provider.NonghyupTransactionProvider;
import samryong.domain.member.entity.Member;
import samryong.global.code.GlobalErrorCode;
import samryong.global.exception.GlobalException;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final NonghyupTransactionProvider nonghyupTransactionProvider;

    // 사용자-> 운영자 계좌로 대여료 송금
    @Override
    @Transactional
    public void drawTransfer(Member sender, Member receiver, Long fee) {

        checkAccount(sender, receiver); // 계좌 유무 확인
        String finTechAccountNum = getAccount(sender).getFinTechAccountNum();

        nonghyupTransactionProvider.drawingTransfer(
                NonghyupBankConverter.toWithdrawRequestDTO(finTechAccountNum, fee, "대여료 출금"));
    }

    // 운영자-> 사용자 계좌로 대여료 송금
    @Override
    @Transactional
    public void receiveTransfer(Member receiver, Long fee) {

        checkAccount(receiver); // 계좌 유무 확인
        String accountNum = getAccount(receiver).getAccountNum();

        nonghyupTransactionProvider.receivedTransferAccountNumber(
                NonghyupBankConverter.toDepositRequestDTO(accountNum, fee, "대여료 입금"));
    }

    @Override
    public Account getAccount(Member member) {
        return member.getAccountList().stream()
                .findFirst()
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.NO_ACCOUNT_REGISTERED));
    }

    @Transactional
    public void checkAccount(Member sender, Member receiver) {
        if (sender.getAccountList().isEmpty()) {
            throw new GlobalException(GlobalErrorCode.NO_ACCOUNT_REGISTERED);
        }
        if (receiver.getAccountList().isEmpty()) {
            throw new GlobalException(GlobalErrorCode.NO_ACCOUNT_RECEIVER);
        }
    }

    private void checkAccount(Member member) {
        if (member.getAccountList().isEmpty()) {
            throw new GlobalException(GlobalErrorCode.NO_ACCOUNT_REGISTERED);
        }
    }
}
