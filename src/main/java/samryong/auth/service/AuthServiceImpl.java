package samryong.auth.service;

import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import samryong.auth.dto.AuthResponseDTO.AuthToken;
import samryong.auth.dto.AuthResponseDTO.LoginResponse;
import samryong.redis.service.RefreshTokenService;
import samryong.security.provider.KakaoAuthProvider;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final KakaoAuthProvider kakaoAuthProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    public LoginResponse kakaoLogin(String code) {

        String accessToken = kakaoAuthProvider.getAccessToken(code); // 1. 인가 코드로 토큰 발급
        HashMap<String, Object> userInfo =
                kakaoAuthProvider.getKakaoUserInfo(accessToken); // 2. 토큰으로 카카오 유저 정보 가져오기
        LoginResponse kakaoUserResponse =
                kakaoAuthProvider.kakaoUserLogin(userInfo); // 3. 카카오 유저 정보로 로그인

        return kakaoUserResponse;
    }

    @Override
    public AuthToken reissueToken(String refreshToken) {
        return refreshTokenService.reissueToken(refreshToken);
    }
}
