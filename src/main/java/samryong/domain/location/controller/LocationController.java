package samryong.domain.location.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import samryong.domain.location.dto.LocationDTO.LocationRequestDTO;
import samryong.domain.location.service.LocationService;
import samryong.domain.member.entity.Member;
import samryong.global.annotation.AuthMember;
import samryong.global.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/location")
@Tag(name = "Location", description = "위치 관련 API")
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    @Operation(summary = "관리자의 위치 등록 API", description = "위치를 등록합니다.")
    public ApiResponse<Long> createLocation(
            @AuthMember Member member, @RequestBody @Valid LocationRequestDTO locationDTO) {
        return ApiResponse.onSuccess("위치 등록 성공", locationService.createLocation(locationDTO));
    }
}
