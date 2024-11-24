package samryong.domain.member.service;

import samryong.domain.account.dto.NonghyupAccountDTO.NonghyupAccountRequestDTO;
import samryong.domain.account.dto.NonghyupAccountDTO.NonghyupAccountResponseDTO;
import samryong.domain.location.dto.LocationDTO.LocationRequestDTO;
import samryong.domain.location.dto.LocationDTO.LocationResponseDTO;
import samryong.domain.member.dto.MemberDTO.MyInformationResponseDTO;
import samryong.domain.member.entity.Member;
import samryong.domain.rent.entity.Rent;

public interface MemberService {

    Member getMember(Long memberId);

    NonghyupAccountResponseDTO registerAccount(Member member, NonghyupAccountRequestDTO requestDTO);

    LocationResponseDTO registerLocation(Member member, LocationRequestDTO requestDTO);

    MyInformationResponseDTO getMyPage(Long memberId);

    void updateMannerRate(Member member, Long mannerRate);

    void updateRentList(Member member, Rent rent);

    void updateLoanList(Member member, Rent rent);

    void updateWishList(Member member, Long itemId);
}
