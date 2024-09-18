package samryong.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import samryong.domain.chat.dto.ChatMessageDTO.ChatMessageRequestDTO;
import samryong.domain.chat.service.ChatMessageService;
import samryong.global.response.ApiResponse;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    // "채팅 메시지 전송"
    @MessageMapping("/message")
    public ApiResponse<String> message(ChatMessageRequestDTO requestDTO) {
        chatMessageService.publishMessage(requestDTO);
        return ApiResponse.onSuccess("메시지 전송 성공", "");
    }
}
