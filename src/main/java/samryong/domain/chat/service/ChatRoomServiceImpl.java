package samryong.domain.chat.service;

import static samryong.global.code.GlobalErrorCode.ITEM_NOT_FOUND;
import static samryong.global.code.GlobalErrorCode.MEMBER_NOT_FOUND;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import samryong.domain.chat.converter.ChatRoomConverter;
import samryong.domain.chat.dto.ChatRoomDTO.ChatRoomHashDTO;
import samryong.domain.chat.dto.ChatRoomDTO.ChatRoomRequestDTO;
import samryong.domain.chat.entity.ChatRoom;
import samryong.domain.chat.redis.RedisSubscriber;
import samryong.domain.chat.repository.ChatRoomRepository;
import samryong.domain.item.entity.Item;
import samryong.domain.item.repository.ItemRepository;
import samryong.domain.member.entity.Member;
import samryong.domain.member.repository.MemberRepository;
import samryong.global.code.GlobalErrorCode;
import samryong.global.exception.GlobalException;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisSubscriber redisSubscriber;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    private static final String CHAT_Rooms = "CHAT_ROOM";
    private HashOperations<String, String, ChatRoomHashDTO> opsHashChatRoom;
    private Map<String, ChannelTopic> topics;

    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
        topics = new HashMap<>();
    }

    // 채팅방 생성
    @Override
    @Transactional
    public Long createChatRoom(Member renter, ChatRoomRequestDTO requestDTO) {
        Member owner =
                memberRepository
                        .findById(requestDTO.getOwnerId())
                        .orElseThrow(() -> new GlobalException(MEMBER_NOT_FOUND));

        Item item =
                itemRepository
                        .findById(requestDTO.getItemId())
                        .orElseThrow(() -> new GlobalException(ITEM_NOT_FOUND));

        if (chatRoomRepository.findByRenterAndOwnerAndItem(renter, owner, item).isPresent()) {
            return chatRoomRepository.findByRenterAndOwnerAndItem(renter, owner, item).get().getId();
        }

        ChatRoom chatRoom = ChatRoomConverter.toChatRoom(renter, owner, item);
        Long chatRoomId = chatRoomRepository.save(chatRoom).getId();

        opsHashChatRoom.put(
                CHAT_Rooms, chatRoomId.toString(), ChatRoomConverter.toChatRoomHashDTO(chatRoom));

        messagingTemplate.convertAndSend("/topic/chat/notification/" + owner.getId(), chatRoom.getId());
        return chatRoomId;
    }

    // 채팅방 입장
    @Override
    @Transactional
    public void enterChatRoom(Long chatRoomId) {

        chatRoomRepository
                .findById(chatRoomId)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.CHAT_ROOM_NOT_FOUND));

        ChannelTopic topic = getTopic(chatRoomId);
        if (topic == null) {
            topic = new ChannelTopic(chatRoomId.toString());
            topics.put(chatRoomId.toString(), topic);
            redisMessageListenerContainer.addMessageListener(redisSubscriber, topic);
        }
    }

    // redis 채널에서 채팅방 조회
    @Override
    public ChannelTopic getTopic(Long chatRoomId) {
        return topics.get(chatRoomId.toString());
    }

    @Override
    public boolean isMemberOfChatRoom(Long roomId, Long memberId) {

        ChatRoom chatRoom =
                chatRoomRepository
                        .findById(roomId)
                        .orElseThrow(() -> new GlobalException(GlobalErrorCode.CHAT_ROOM_NOT_FOUND));

        return chatRoom.getRenter().getId().equals(memberId)
                || chatRoom.getOwner().getId().equals(memberId);
    }
}
