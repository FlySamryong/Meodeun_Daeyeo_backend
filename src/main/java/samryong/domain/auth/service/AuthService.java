package samryong.domain.auth.service;

import samryong.domain.auth.dto.AuthResponseDTO;

public interface AuthService {

    AuthResponseDTO.LoginResponse kakaoLogin(String code);

    AuthResponseDTO.AuthToken reissueToken(String refreshToken);
}
