package samryong.domain.redis.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import samryong.domain.member.entity.Member;
import samryong.domain.redis.service.RecentItemService;
import org.springframework.web.bind.annotation.*;
import samryong.security.resolver.annotation.AuthMember;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/redis/")
@Tag(name = "redis", description = "redis관련")
public class ItemController {

    private final RecentItemService recentItemService;

    // 아이템 상세 조회 API
    @GetMapping("/item/{itemId}")
    @Operation(summary = "아이템 상세 조회", description = "사용자가 물품을 등록합니다.")
    public String getItemDetails(@PathVariable Long itemId, @AuthMember Member member) {
        recentItemService.saveRecentItem(member, itemId);
        return "forward:/api/item/{itemId}"; // item 상세페이지 조회로 보냄
    }

    @GetMapping("/api/item/recent")
    @Operation(summary = "최근 본 아이템 조회", description = "사용자가 최근 본 아이템을 조회합니다.")
    public String getRecentItems(@AuthMember Member member, Model model) {
        // Redis에서 최근 본 아이템 목록 가져오기
        List<Object> recentItems = recentItemService.getRecentItems(member);

        // Model에 최근 본 아이템 목록 추가
        model.addAttribute("recentItemIds", recentItems);

        // 다른 URL로 forward
        return "forward:/api/item/recent";
}