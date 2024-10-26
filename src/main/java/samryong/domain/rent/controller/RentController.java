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
import samryong.domain.rent.service.RentService;
import samryong.global.annotation.AuthChatMember;
import samryong.global.annotation.AuthMember;
import samryong.global.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rent")
@Tag(name = "Rent", description = "대여 관련 API")
public class RentController {

    private final RentService rentService;

    @Operation(summary = "대여로 송금하기", description = "대여를 시작하기 위해 대여료를 송금합니다.")
    @GetMapping("/create")
    @AuthChatMember
    public ApiResponse<Long> sendRentFee(
            @AuthMember Member renter,
            @RequestParam(value = "roomId") Long roomId,
            @RequestParam(value = "fee") Long fee) {
        return ApiResponse.onSuccess("대여료 송금 성공", rentService.sendRentFee(renter, roomId, fee));
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
                "대여 수락 성공", rentService.acceptRent(owner, roomId, rentId, endDate));
    }
}
