package samryong.auth.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthResponseDTO {

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LoginResponse {
        private Long id;
        private String name;
        private AuthToken token;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class AuthToken {
        private String accessToken;
        private String refreshToken;
    }
}
