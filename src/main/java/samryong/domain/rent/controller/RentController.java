package samryong.domain.rent.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import samryong.domain.member.entity.Member;
import samryong.domain.rent.service.RentRequestService;
import samryong.domain.rent.service.RentReturnService;
import samryong.global.annotation.AuthChatMember;
import samryong.global.annotation.AuthMember;
import samryong.global.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rent")
@Tag(name = "Rent", description = "대여 관련 API")
public class RentController {

    private final RentRequestService rentRequestService;
    private final RentReturnService rentReturnService;

    @Operation(summary = "대여로 송금하기", description = "대여를 시작하기 위해 대여료를 송금합니다.")
    @GetMapping("/create")
    @AuthChatMember
    public ApiResponse<Long> sendRentFee(
            @AuthMember Member renter,
            @RequestParam(value = "roomId") Long roomId,
            @RequestParam(value = "fee") Long fee) {
        return ApiResponse.onSuccess("대여료 송금 성공", rentRequestService.sendRentFee(renter, roomId, fee));
    }

    @Operation(summary = "대여 수락하기", description = "요청이 이루어진 대여를 수락합니다.")
    @GetMapping("/accept")
    @AuthChatMember
    public ApiResponse<Long> acceptRent(
            @AuthMember Member owner,
            @RequestParam(value = "roomId") Long roomId,
            @RequestParam(value = "rentId") Long rentId,
            @RequestParam(value = "endDate")
                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
                    @Schema(description = "대여 기간 만료 날짜", example = "2024-10-30 15:30", type = "string")
                    LocalDateTime endDate) {
        return ApiResponse.onSuccess(
                "대여 수락 성공", rentRequestService.acceptRent(owner, roomId, rentId, endDate));
    }

    @Operation(summary = "최종 대여 동의 후 대여 진행", description = "대여를 최종 동의하고 대여가 진행됩니다.")
    @GetMapping("/accept-process")
    @AuthChatMember
    public ApiResponse<Long> acceptRentProcess(
            @AuthMember Member renter,
            @RequestParam(value = "roomId") Long roomId,
            @RequestParam(value = "rentId") Long rentId) {
        return ApiResponse.onSuccess(
                "대여 동의 성공", rentRequestService.acceptRentProcess(renter, roomId, rentId));
    }

    @Operation(summary = "반납 진행을 위한 OTP 생성", description = "등록자가 반납 인증 절차를 위한 OTP를 생성합니다.")
    @GetMapping("/generate-otp")
    @AuthChatMember
    public ApiResponse<String> generateOTP(
            @AuthMember Member owner,
            @RequestParam(value = "roomId") Long roomId,
            @RequestParam(value = "rentId") Long rentId) {
        return ApiResponse.onSuccess("OTP 생성 성공", rentReturnService.generateOTP(owner, roomId, rentId));
    }

    @Operation(summary = "OTP 검증 및 대여 정보 변경", description = "대여자가 OTP를 검증하고 대여 진행을 위한 정보를 변경합니다.")
    @GetMapping("/verify-otp")
    @AuthChatMember
    public ApiResponse<String> verifyOTP(
            @AuthMember Member renter,
            @RequestParam(value = "roomId") Long roomId,
            @RequestParam(value = "rentId") Long rentId,
            @RequestParam(value = "otp") String otp) {
        rentReturnService.verifyOTP(renter, roomId, rentId, otp);
        return ApiResponse.onSuccess("OTP 검증 성공", null);
    }

    @Operation(summary = "보증금 반환", description = "대여자가 보증금을 반환받는 경우 true, 아닌 경우 false를 전달합니다.")
    @GetMapping("/return-deposit")
    @AuthChatMember
    public ApiResponse<String> returnDeposit(
            @AuthMember Member owner,
            @RequestParam(value = "roomId") Long roomId,
            @RequestParam(value = "rentId") Long rentId,
            @RequestParam(value = "isReturn") Boolean isReturn) {
        rentReturnService.returnDeposit(owner, roomId, rentId, isReturn);
        return ApiResponse.onSuccess("보증금 반환 성공", null);
    }
}
