package samryong.domain.chat.converter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import samryong.domain.chat.dto.ChatMessageDTO.ChatMessageRequestDTO;
import samryong.domain.chat.dto.ChatMessageDTO.ChatMessageResponseDTO;
import samryong.domain.chat.dto.ChatMessageDTO.ChatMessageResponseListDTO;
import samryong.domain.chat.entity.ChatMessage;
import samryong.domain.member.entity.Member;

@Component
public class ChatMessageConverter {

    public static ChatMessage toChatMessage(ChatMessageRequestDTO requestDTO) {

        return ChatMessage.builder()
                .chatRoomId(requestDTO.getChatRoomId())
                .senderId(requestDTO.getSenderId())
                .message(requestDTO.getMessage())
                .imageUri(requestDTO.getImageCode())
                .createdAt(LocalDateTime.now())
                .type(requestDTO.getType())
                .build();
    }

    public static ChatMessageResponseDTO toChatMessageResponseDTO(ChatMessage chatMessage) {
        return ChatMessageResponseDTO.builder()
                .chatRoomId(chatMessage.getChatRoomId())
                .senderId(chatMessage.getSenderId())
                .message(chatMessage.getMessage())
                .imageUri(chatMessage.getImageUri())
                .createdAt(chatMessage.getCreatedAt())
                .type(chatMessage.getType())
                .build();
    }

    public static ChatMessageResponseListDTO toChatMessageResponseListDTO(
            List<ChatMessage> chatMessageList) {
        return ChatMessageResponseListDTO.builder()
                .chatMessageList(
                        chatMessageList.stream()
                                .map(ChatMessageConverter::toChatMessageResponseDTO)
                                .collect(Collectors.toList()))
                .build();
    }

    public static ChatMessageRequestDTO toRentRequestChatMessage(Member sender, Long chatRoomId) {
        return ChatMessageRequestDTO.builder()
                .chatRoomId(chatRoomId)
                .senderId(sender.getId())
                .message("대여료를 송금했습니다. 12시간 내로 확인해주세요.")
                .type(ChatMessage.ChatType.RENT_REQ)
                .build();
    }

    public static ChatMessageRequestDTO toRentAcceptChatMessage(Member sender, Long chatRoomId) {
        return ChatMessageRequestDTO.builder()
                .chatRoomId(chatRoomId)
                .senderId(sender.getId())
                .message("물품 대여 요청이 수락되었습니다. 대여 기간 동안 잘 사용해주세요.")
                .type(ChatMessage.ChatType.RENT_ACCEPT)
                .build();
    }
}
