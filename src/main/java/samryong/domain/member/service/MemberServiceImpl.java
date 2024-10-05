package samryong.domain.member.service;

import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import samryong.domain.account.converter.AccountConverter;
import samryong.domain.account.dto.NonghyupAccountDTO.NonghyupAccountRequestDTO;
import samryong.domain.account.dto.NonghyupAccountDTO.NonghyupAccountResponseDTO;
import samryong.domain.account.entity.Account;
import samryong.domain.account.repository.AccountRepository;
import samryong.domain.bank.nonghyup.provider.NonghyupTransactionProvider;
import samryong.domain.location.converter.LocationConverter;
import samryong.domain.member.dto.memberDTO;
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
    public memberDTO.MemberResponseDTO getMyPage(Long memberId) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);

        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();

            return new memberDTO.MemberResponseDTO(
                    member.getNickName(),
                    member.getEmail(),
                    member.getProfileImage(),
                    member.getMannerRate(),
                    LocationConverter.toResponseDTO(member.getLocation()),
                    member.getAccountList().stream()
                            .map(account -> AccountConverter.toAccountResponseDTO(account))
                            .toList());
        } else {
            throw new GlobalException(GlobalErrorCode._NOT_FOUND);
        }
    }
}
