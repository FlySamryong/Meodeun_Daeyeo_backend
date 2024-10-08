package samryong.domain.chat.service;

import static samryong.global.code.GlobalErrorCode.ITEM_NOT_FOUND;
import static samryong.global.code.GlobalErrorCode.MEMBER_NOT_FOUND;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import samryong.domain.chat.converter.ChatRoomConverter;
import samryong.domain.chat.dto.ChatRoomDTO.ChatRoomHashDTO;
import samryong.domain.chat.dto.ChatRoomDTO.ChatRoomListResponseDTO;
import samryong.domain.chat.dto.ChatRoomDTO.ChatRoomRequestDTO;
import samryong.domain.chat.dto.ChatRoomDTO.ChatRoomResponseDTO;
import samryong.domain.chat.entity.ChatMessage;
import samryong.domain.chat.entity.ChatRoom;
import samryong.domain.chat.redis.RedisSubscriber;
import samryong.domain.chat.repository.ChatMessageRepository;
import samryong.domain.chat.repository.ChatRoomRepository;
import samryong.domain.item.entity.Item;
import samryong.domain.item.repository.ItemRepository;
import samryong.domain.member.entity.Member;
import samryong.domain.member.repository.MemberRepository;
import samryong.domain.member.service.MemberService;
import samryong.global.code.GlobalErrorCode;
import samryong.global.exception.GlobalException;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final MemberRepository memberRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ItemRepository itemRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisSubscriber redisSubscriber;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;
    private final RedisTemplate<String, ChatMessage> redisTemplateMessage;
    private final MemberService memberService;

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

    // 채팅방 목록 조회
    @Override
    public ChatRoomListResponseDTO getChatRoomList(Member member) {

        List<ChatRoomResponseDTO> chatRoomDTOList = new ArrayList<>();

        Sort sort = Sort.by(Sort.Direction.DESC, "updatedDate");
        List<ChatRoom> chatRoomList = chatRoomRepository.findAllByRenterOrOwner(member, member, sort);

        for (ChatRoom chatRoom : chatRoomList) {
            ChatRoomResponseDTO chatRoomDTO =
                    ChatRoomConverter.toChatRoomResponseDTO(chatRoom, getLastMessage(chatRoom.getId()));
            chatRoomDTOList.add(chatRoomDTO);
        }

        return ChatRoomConverter.toChatRoomListResponseDTO(chatRoomDTOList);
    }

    // 채팅 상대방 매너 온도 업데이트
    @Override
    public void updateMannerRate(Member member, Long roomId, Long mannerRate) {

        ChatRoom chatRoom =
                chatRoomRepository
                        .findById(roomId)
                        .orElseThrow(() -> new GlobalException(GlobalErrorCode.CHAT_ROOM_NOT_FOUND));

        Member targetMember =
                member.getId().equals(chatRoom.getOwner().getId())
                        ? chatRoom.getRenter()
                        : chatRoom.getOwner();

        memberService.updateMannerRate(targetMember, mannerRate);
    }

    // 채팅방 마지막 메시지 업데이트
    @Override
    public void updateChatRoomLastMessage(Long chatRoomId) {
        ChatRoom chatRoom =
                chatRoomRepository
                        .findById(chatRoomId)
                        .orElseThrow(() -> new GlobalException(GlobalErrorCode.CHAT_ROOM_NOT_FOUND));

        chatRoom.setUpdatedDate(LocalDateTime.now());
        chatRoomRepository.save(chatRoom);
    }

    // 채팅방 마지막 메시지 조회
    @Override
    public ChatMessage getLastMessage(Long roomId) {

        ChatMessage latestMessage = redisTemplateMessage.opsForList().index(roomId.toString(), 0);

        if (latestMessage == null) {

            latestMessage = chatMessageRepository.findTopByChatRoomIdOrderByCreatedAtDesc(roomId);

            if (latestMessage == null) return null;

            redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessage.class));
            redisTemplateMessage.opsForList().leftPush(roomId.toString(), latestMessage);
        }

        return latestMessage;
    }

    // redis 채널에서 채팅방 조회
    @Override
    public ChannelTopic getTopic(Long chatRoomId) {
        return topics.get(chatRoomId.toString());
    }

    // 채팅방 멤버 확인
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
