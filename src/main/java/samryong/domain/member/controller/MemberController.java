package samryong.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import samryong.domain.account.dto.NonghyupAccountDTO.NonghyupAccountRequestDTO;
import samryong.domain.account.dto.NonghyupAccountDTO.NonghyupAccountResponseDTO;
import samryong.domain.location.dto.LocationDTO.LocationRequestDTO;
import samryong.domain.location.dto.LocationDTO.LocationResponseDTO;
import samryong.domain.member.dto.MemberDTO.MyInformationResponseDTO;
import samryong.domain.member.entity.Member;
import samryong.domain.member.service.MemberService;
import samryong.domain.rent.dto.RentDTO;
import samryong.global.annotation.AuthMember;
import samryong.global.response.ApiResponse;

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

    @Operation(summary = "내 대여 목록 조회", description = "현재 진행 중인 대여 목록을 조회합니다.")
    @GetMapping("/myRentOrLoanList")
    public ApiResponse<RentDTO.MyRentOrLoanResponseListDTO> getMyRentOrLoanList(
            @AuthMember Member member) {
        return ApiResponse.onSuccess("대여 목록 조회 성공", memberService.getMyRentOrLoanList(member));
    }

    @Operation(summary = "마이페이지 정보 조회", description = "농협 계좌 정보를 입력 받아서 계좌 정보를 등록합니다.")
    @GetMapping("/myPage")
    public ApiResponse<MyInformationResponseDTO> getMyPage(@AuthMember Member member) {
        return ApiResponse.onSuccess("마이페이지 조회 성공", memberService.getMyPage(member.getId()));
    }

    @Operation(summary = "관심 목록 등록", description = "아이템을 관심 목록에 추가합니다.")
    @GetMapping("/wishList/{itemId}")
    public ApiResponse<?> getWishList(@AuthMember Member member, @PathVariable Long itemId) {
        memberService.updateWishItemList(member, itemId);
        return ApiResponse.onSuccess("관심 목록 등록 성공");
    }

    @Operation(summary = "사용자 주소 등록", description = "사용자의 주소를 입력받아 등록합니다.")
    @PostMapping("/register/location")
    public ApiResponse<LocationResponseDTO> registerLocation(
            @AuthMember Member member, @Valid @RequestBody LocationRequestDTO requestDTO) {
        return ApiResponse.onSuccess("주소지 등록 성공", memberService.registerLocation(member, requestDTO));
    }
}
