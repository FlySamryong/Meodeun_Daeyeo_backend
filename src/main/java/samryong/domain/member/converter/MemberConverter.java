package samryong.domain.member.converter;

import org.springframework.stereotype.Component;
import samryong.domain.account.converter.AccountConverter;
import samryong.domain.location.converter.LocationConverter;
import samryong.domain.member.dto.MemberDTO.MyInformationResponseDTO;
import samryong.domain.member.entity.Member;

@Component
public class MemberConverter {

    public static MyInformationResponseDTO toMemberResponseDTO(Member member) {

        return MyInformationResponseDTO.builder()
                .nickName(member.getNickName())
                .email(member.getEmail())
                .profileImage(member.getProfileImage().getImageUri())
                .mannerRate(member.getMannerRate())
                .location(LocationConverter.toLocationResponseDTO(member.getLocation()))
                .accountList(AccountConverter.toAccountResponseListDTO(member.getAccountList()))
                .build();
    }
}
