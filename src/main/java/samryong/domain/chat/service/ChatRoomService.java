package samryong.domain.chat.service;

import org.springframework.data.redis.listener.ChannelTopic;
import samryong.domain.chat.dto.ChatRoomDTO.ChatRoomListResponseDTO;
import samryong.domain.chat.dto.ChatRoomDTO.ChatRoomRequestDTO;
import samryong.domain.chat.entity.ChatMessage;
import samryong.domain.member.entity.Member;

public interface ChatRoomService {

    Long createChatRoom(Member renter, ChatRoomRequestDTO requestDTO);

    void enterChatRoom(Long chatRoomId);

    ChannelTopic getTopic(Long chatRoomId);

    boolean isMemberOfChatRoom(Long roomId, Long memberId);

    ChatRoomListResponseDTO getChatRoomList(Member member);

    void updateChatRoomLastMessage(Long chatRoomId);

    ChatMessage getLastMessage(Long roomId);

    void updateMannerRate(Member member, Long roomId, Long mannerRate);
}
