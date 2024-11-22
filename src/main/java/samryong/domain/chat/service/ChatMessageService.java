package samryong.domain.chat.service;

import samryong.domain.chat.dto.ChatMessageDTO.ChatMessageRequestDTO;
import samryong.domain.chat.dto.ChatMessageDTO.ChatMessageResponseDTO;
import samryong.domain.chat.dto.ChatMessageDTO.ChatMessageResponseListDTO;
import samryong.domain.chat.entity.ChatMessage.ChatType;
import samryong.domain.member.entity.Member;

public interface ChatMessageService {

    void publishMessage(ChatMessageRequestDTO requestDTO);

    ChatMessageResponseListDTO getMessageList(Long roomId);

    void saveMessage(ChatMessageResponseDTO responseDTO);

    void sendRentCommonMessage(Member member, Long roomId, String message, ChatType type);

    void sendRentRequestMessage(
            Member member, Long roomId, Long rentId, String message, ChatType type);
}
