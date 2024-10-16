package samryong.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import samryong.domain.auth.dto.AuthResponseDTO;
import samryong.domain.auth.service.AuthService;
import samryong.domain.member.entity.Member;
import samryong.global.annotation.AuthMember;
import samryong.global.annotation.GetToken;
import samryong.global.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "인증 및 로그인 관련 API")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "카카오 로그인", description = "인가 코드를 입력 받아서 카카오 로그인을 합니다.")
    @GetMapping("/kakao/login")
    public ApiResponse<AuthResponseDTO.LoginResponse> kakaoLogin(@RequestParam("code") String code) {
        return ApiResponse.onSuccess("카카오 로그인 성공", authService.kakaoLogin(code));
    }

    @Operation(summary = "토큰 재발급", description = "access 토큰이 만료된 경우 refresh 토큰을 통해 토큰을 재발급 합니다.")
    @PostMapping("/kakao/reissue")
    public ApiResponse<AuthResponseDTO.AuthToken> reissueToken(@GetToken String refreshToken) {
        return ApiResponse.onSuccess("토큰 재발급 성공", authService.reissueToken(refreshToken));
    }

    @GetMapping("/test")
    public ApiResponse<String> testAuth(@AuthMember Member member) {
        return ApiResponse.onSuccess("인증 성공", "인증 성공 + " + member.getId());
    }
}
