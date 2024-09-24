package samryong.domain.chat.service;

import org.springframework.data.redis.listener.ChannelTopic;
import samryong.domain.chat.dto.ChatRoomDTO.ChatRoomRequestDTO;
import samryong.domain.member.entity.Member;

public interface ChatRoomService {

    Long createChatRoom(Member renter, ChatRoomRequestDTO requestDTO);

    void enterChatRoom(Long chatRoomId);

    ChannelTopic getTopic(Long chatRoomId);

    boolean isMemberOfChatRoom(Long roomId, Long memberId);
}
