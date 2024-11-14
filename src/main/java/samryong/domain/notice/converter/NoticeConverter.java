package samryong.domain.notice.converter;

import org.springframework.stereotype.Component;
import samryong.domain.chat.dto.ChatMessageDTO.ChatMessageRequestDTO;
import samryong.domain.chat.entity.ChatMessage;
import samryong.domain.chat.entity.ChatRoom;

@Component
public class NoticeConverter {
    public static ChatMessageRequestDTO toChatMessageRequestDTO(ChatRoom chatRoom, String message) {
        return ChatMessageRequestDTO.builder()
                .chatRoomId(chatRoom.getId())
                .senderId(chatRoom.getOwner().getId())
                .message(message)
                .type(ChatMessage.ChatType.NOTICE)
                .build();
    }
}
