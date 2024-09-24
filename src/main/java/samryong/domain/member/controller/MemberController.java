package samryong.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import samryong.domain.account.dto.NonghyupAccountDTO.NonghyupAccountRequestDTO;
import samryong.domain.account.dto.NonghyupAccountDTO.NonghyupAccountResponseDTO;
import samryong.domain.member.entity.Member;
import samryong.domain.member.service.MemberService;
import samryong.global.response.ApiResponse;
import samryong.security.resolver.annotation.AuthMember;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Tag(name = "Member", description = "회원 관련 API")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "농협 입출금 계좌 등록", description = "농협 계좌 정보를 입력 받아서 계좌 정보를 등록합니다.")
    @PostMapping("/register/account/nh")
    public ApiResponse<NonghyupAccountResponseDTO> registerAccount(
            @AuthMember Member member, @Valid @RequestBody NonghyupAccountRequestDTO requestDTO) {
        return ApiResponse.onSuccess(
                "농협 입출금 계좌 등록 성공", memberService.registerAccount(member, requestDTO));
    }
}
