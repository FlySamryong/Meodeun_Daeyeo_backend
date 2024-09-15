package samryong.domain.redis.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import samryong.domain.auth.dto.AuthResponseDTO;
import samryong.domain.auth.dto.AuthResponseDTO.AuthToken;
import samryong.domain.member.repository.MemberRepository;
import samryong.domain.redis.entity.RefreshToken;
import samryong.domain.redis.repository.RefreshTokenRepository;
import samryong.global.code.GlobalErrorCode;
import samryong.global.exception.GlobalException;
import samryong.security.provider.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void saveRefreshToken(String refreshToken) {
        RefreshToken token =
                RefreshToken.builder()
                        .id(jwtTokenProvider.getMemberId(refreshToken))
                        .token(refreshToken)
                        .build();
        refreshTokenRepository.save(token);
    }

    @Transactional
    public AuthToken reissueToken(String refreshToken) {
        jwtTokenProvider.validateToken(refreshToken); // refresh token 유효성 검사

        RefreshToken oldRefreshToken =
                refreshTokenRepository
                        .findById(jwtTokenProvider.getMemberId(refreshToken))
                        .orElseThrow(() -> new GlobalException(GlobalErrorCode.AUTH_INVALID_REFRESH_TOKEN));

        if (!oldRefreshToken
                .getToken()
                .equals(refreshToken)) { // 저장된 refresh token과 요청된 refresh token이 다를 경우
            throw new GlobalException(GlobalErrorCode.AUTH_INVALID_REFRESH_TOKEN);
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(oldRefreshToken.getId());
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(oldRefreshToken.getId());

        saveRefreshToken(newRefreshToken);

        return AuthResponseDTO.AuthToken.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }
}
