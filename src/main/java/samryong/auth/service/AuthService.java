package samryong.auth.service;

import samryong.auth.dto.AuthResponseDTO.AuthToken;
import samryong.auth.dto.AuthResponseDTO.LoginResponse;

public interface AuthService {

    LoginResponse kakaoLogin(String code);

    AuthToken reissueToken(String refreshToken);
}
