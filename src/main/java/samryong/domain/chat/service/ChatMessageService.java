package samryong.domain.chat.service;

import samryong.domain.chat.dto.ChatMessageDTO.ChatMessageRequestDTO;
import samryong.domain.chat.dto.ChatMessageDTO.ChatMessageResponseListDTO;
import samryong.domain.chat.entity.ChatMessage.ChatType;
import samryong.domain.member.entity.Member;

public interface ChatMessageService {

    void publishMessage(ChatMessageRequestDTO requestDTO);

    ChatMessageResponseListDTO getMessageList(Long roomId);

    void saveMessage(ChatMessageRequestDTO requestDTO);

    void sendRentActivityMessage(Member member, Long roomId, String message, ChatType type);
}
