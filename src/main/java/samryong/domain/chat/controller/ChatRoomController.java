package samryong.domain.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import samryong.domain.chat.aspect.annotation.AuthChatMember;
import samryong.domain.chat.dto.ChatMessageDTO.ChatMessageResponseListDTO;
import samryong.domain.chat.dto.ChatRoomDTO.ChatRoomRequestDTO;
import samryong.domain.chat.service.ChatMessageService;
import samryong.domain.chat.service.ChatRoomService;
import samryong.domain.member.entity.Member;
import samryong.global.response.ApiResponse;
import samryong.security.resolver.annotation.AuthMember;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
@Tag(name = "Chat", description = "채팅 관련 API")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    @Operation(summary = "채팅방 생성", description = "거래를 하기 위한 일대일 채팅방을 생성합니다.")
    @PostMapping("/room/create")
    public ApiResponse<Long> createChatRoom(
            @AuthMember Member renter, @Valid @RequestBody ChatRoomRequestDTO requestDTO) {
        return ApiResponse.onSuccess("채팅방 생성 성공", chatRoomService.createChatRoom(renter, requestDTO));
    }

    @GetMapping("/room/messageList")
    @AuthChatMember // 채팅방 멤버인지 우선 확인
    public ApiResponse<ChatMessageResponseListDTO> getMessageList(
            @RequestParam(value = "roomId") Long roomId) {
        return ApiResponse.onSuccess("채팅방 메시지 조회 성공", chatMessageService.getMessageList(roomId));
    }
}
