package samryong.domain.auth.service;

import samryong.domain.auth.dto.AuthResponseDTO;

public interface AuthService {

    AuthResponseDTO.LoginResponse kakaoLogin(String accessToken);

    AuthResponseDTO.AuthToken reissueToken(String refreshToken);
}
