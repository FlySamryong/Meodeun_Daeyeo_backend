package samryong.domain.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import samryong.domain.account.converter.AccountConverter;
import samryong.domain.account.dto.NonghyupAccountDTO.NonghyupAccountRequestDTO;
import samryong.domain.account.dto.NonghyupAccountDTO.NonghyupAccountResponseDTO;
import samryong.domain.account.entity.Account;
import samryong.domain.account.repository.AccountRepository;
import samryong.domain.bank.nonghyup.provider.NonghyupTransactionProvider;
import samryong.domain.member.converter.MemberConverter;
import samryong.domain.member.dto.MemberDTO.MyInformationResponseDTO;
import samryong.domain.member.entity.Member;
import samryong.domain.member.repository.MemberRepository;
import samryong.global.code.GlobalErrorCode;
import samryong.global.exception.GlobalException;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;
    private final NonghyupTransactionProvider nonghyupTransactionProvider;

    @Override
    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElse(null);
    }

    @Override
    @Transactional
    public NonghyupAccountResponseDTO registerAccount(
            Member member, NonghyupAccountRequestDTO requestDTO) {

        String accountNumber = requestDTO.getAccountNumber();
        String rgNum = nonghyupTransactionProvider.openFinAccountDirect(accountNumber);
        String finTechAccountNum = nonghyupTransactionProvider.checkOpenFinAccountDirect(rgNum);

        Account account = accountRepository.findByAccountNum(accountNumber).orElse(null);
        if (account != null) {
            throw new GlobalException(GlobalErrorCode.ACCOUNT_ALREADY_EXIST);
        }

        account = AccountConverter.toAccount(accountNumber, finTechAccountNum);
        member.addAccount(account);

        memberRepository.save(member);
        return AccountConverter.toAccountResponseDTO(account);
    }

    @Override
    public MyInformationResponseDTO getMyPage(Long memberId) {

        Member member =
                memberRepository
                        .findById(memberId)
                        .orElseThrow(() -> new GlobalException(GlobalErrorCode.MEMBER_NOT_FOUND));

        return MemberConverter.toMemberResponseDTO(member);
    }
}
