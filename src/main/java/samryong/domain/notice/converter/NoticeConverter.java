package samryong.domain.notice.converter;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;
import samryong.domain.chat.dto.ChatMessageDTO;
import samryong.domain.chat.entity.ChatMessage;
import samryong.domain.chat.entity.ChatRoom;

@Component
public class NoticeConverter {
    public static ChatMessageDTO.ChatMessageRequestDTO toChatMessageRequestDTO(
            ChatRoom chatRoom, String message) {
        return ChatMessageDTO.ChatMessageRequestDTO.builder()
                .chatRoomId(chatRoom.getId())
                .senderId(chatRoom.getOwner().getId())
                .createdAt(LocalDateTime.now())
                .message(message)
                .type(ChatMessage.ChatType.NOTICE)
                .build();
    }
}
