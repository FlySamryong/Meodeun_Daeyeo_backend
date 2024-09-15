package samryong.domain.auth.converter;

import java.util.HashMap;
import org.springframework.stereotype.Component;
import samryong.domain.auth.dto.AuthResponseDTO;
import samryong.domain.member.entity.Member;

@Component
public class KakaoAuthConverter {

    public static Member toMember(HashMap<String, Object> userInfo) {
        return Member.builder()
                .email((String) userInfo.get("email"))
                .nickName((String) userInfo.get("nickname"))
                .build();
    }

    public static AuthResponseDTO.LoginResponse toLoginResponse(
            String accessToken, String refreshToken, Member member) {
        return AuthResponseDTO.LoginResponse.builder()
                .id(member.getId())
                .name(member.getNickName())
                .token(
                        AuthResponseDTO.AuthToken.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .build())
                .build();
    }
}
