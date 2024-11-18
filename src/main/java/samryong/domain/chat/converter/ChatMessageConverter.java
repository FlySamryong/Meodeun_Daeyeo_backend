package samryong.domain.chat.converter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import samryong.domain.chat.dto.ChatMessageDTO.ChatMessageRequestDTO;
import samryong.domain.chat.dto.ChatMessageDTO.ChatMessageResponseDTO;
import samryong.domain.chat.dto.ChatMessageDTO.ChatMessageResponseListDTO;
import samryong.domain.chat.entity.ChatMessage;
import samryong.domain.chat.entity.ChatMessage.ChatType;
import samryong.domain.member.entity.Member;

@Component
public class ChatMessageConverter {

    public static ChatMessage toChatMessage(ChatMessageResponseDTO responseDTO) {
        return ChatMessage.builder()
                .chatRoomId(responseDTO.getChatRoomId())
                .rentId(responseDTO.getRentId())
                .senderId(responseDTO.getSenderId())
                .message(responseDTO.getMessage())
                .imageUri(responseDTO.getImageUri())
                .createdAt(responseDTO.getCreatedAt())
                .type(responseDTO.getType())
                .build();
    }

    public static ChatMessageResponseDTO toChatMessageResponseDTO(ChatMessage chatMessage) {
        return ChatMessageResponseDTO.builder()
                .chatRoomId(chatMessage.getChatRoomId())
                .rentId(chatMessage.getRentId())
                .senderId(chatMessage.getSenderId())
                .message(chatMessage.getMessage())
                .imageUri(chatMessage.getImageUri())
                .createdAt(chatMessage.getCreatedAt())
                .type(chatMessage.getType())
                .build();
    }

    public static ChatMessageResponseDTO toChatMessageResponseDTO(ChatMessageRequestDTO requestDTO) {
        return ChatMessageResponseDTO.builder()
                .chatRoomId(requestDTO.getChatRoomId())
                .rentId(requestDTO.getRentId())
                .senderId(requestDTO.getSenderId())
                .message(requestDTO.getMessage())
                .imageUri(requestDTO.getImageUri())
                .createdAt(LocalDateTime.now())
                .type(requestDTO.getType())
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

    public static ChatMessageRequestDTO toRentMessage(
            Member sender, Long chatRoomId, String message, ChatType type) {
        return ChatMessageRequestDTO.builder()
                .chatRoomId(chatRoomId)
                .senderId(sender.getId())
                .message(message)
                .type(type)
                .build();
    }

    public static ChatMessageRequestDTO toRentRequestMessage(
            Member sender, Long chatRoomId, Long rentId, String message, ChatType type) {
        return ChatMessageRequestDTO.builder()
                .chatRoomId(chatRoomId)
                .rentId(rentId)
                .senderId(sender.getId())
                .message(message)
                .type(type)
                .build();
    }
}
