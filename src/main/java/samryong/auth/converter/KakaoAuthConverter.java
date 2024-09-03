package samryong.auth.converter;

import java.util.HashMap;
import org.springframework.stereotype.Component;
import samryong.auth.dto.AuthResponseDTO.AuthToken;
import samryong.auth.dto.AuthResponseDTO.LoginResponse;
import samryong.member.domain.Member;

@Component
public class KakaoAuthConverter {

    public static Member toMember(HashMap<String, Object> userInfo) {
        return Member.builder()
                .email((String) userInfo.get("email"))
                .nicName((String) userInfo.get("nickname"))
                .build();
    }

    public static LoginResponse toLoginResponse(
            String accessToken, String refreshToken, Member member) {
        return LoginResponse.builder()
                .id(member.getId())
                .name(member.getNicName())
                .token(AuthToken.builder().accessToken(accessToken).refreshToken(refreshToken).build())
                .build();
    }
}
