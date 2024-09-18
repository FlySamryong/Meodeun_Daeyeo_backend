package samryong.domain.chat.service;

import samryong.domain.chat.dto.ChatMessageDTO.ChatMessageRequestDTO;
import samryong.domain.chat.dto.ChatMessageDTO.ChatMessageResponseListDTO;

public interface ChatMessageService {

    void publishMessage(ChatMessageRequestDTO requestDTO);

    ChatMessageResponseListDTO getMessageList(Long roomId);

    void saveMessage(ChatMessageRequestDTO requestDTO);
}
