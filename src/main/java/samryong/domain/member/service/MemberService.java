package samryong.domain.member.service;

import samryong.domain.account.dto.NonghyupAccountDTO.NonghyupAccountRequestDTO;
import samryong.domain.account.dto.NonghyupAccountDTO.NonghyupAccountResponseDTO;
import samryong.domain.member.dto.memberDTO;
import samryong.domain.member.entity.Member;

public interface MemberService {

    Member getMember(Long memberId);

    NonghyupAccountResponseDTO registerAccount(Member member, NonghyupAccountRequestDTO requestDTO);

    memberDTO.MemberRequestDTO getMyPage(Long memberId);
}
