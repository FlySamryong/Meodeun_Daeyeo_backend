package samryong.domain.chat.service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;
import samryong.domain.chat.converter.ChatMessageConverter;
import samryong.domain.chat.dto.ChatMessageDTO.ChatMessageRequestDTO;
import samryong.domain.chat.dto.ChatMessageDTO.ChatMessageResponseDTO;
import samryong.domain.chat.dto.ChatMessageDTO.ChatMessageResponseListDTO;
import samryong.domain.chat.entity.ChatMessage;
import samryong.domain.chat.entity.ChatMessage.ChatType;
import samryong.domain.chat.redis.RedisPublisher;
import samryong.domain.chat.repository.ChatMessageRepository;
import samryong.domain.member.entity.Member;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final RedisPublisher redisPublisher;
    private final ChatRoomService chatRoomService;
    private final RedisTemplate<String, ChatMessage> redisTemplateMessage;
    private static final String CHAT_ROOM = "CHAT_ROOM:";

    @Override
    @Transactional
    public void publishMessage(ChatMessageRequestDTO requestDTO) {

        chatRoomService.enterChatRoom(requestDTO.getChatRoomId()); // 리스너와 연동

        ChatMessageResponseDTO chatMessageResponseDTO =
                ChatMessageConverter.toChatMessageResponseDTO(requestDTO);

        redisPublisher.publish(
                chatRoomService.getTopic(requestDTO.getChatRoomId()), chatMessageResponseDTO);

        saveMessage(chatMessageResponseDTO);
    }

    @Override
    @Transactional
    public void saveMessage(ChatMessageResponseDTO responseDTO) {
        ChatMessage chatMessage = ChatMessageConverter.toChatMessage(responseDTO);
        String key = CHAT_ROOM + chatMessage.getChatRoomId();

        chatMessageRepository.save(chatMessage);

        redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessage.class));

        redisTemplateMessage.opsForList().leftPush(key, chatMessage);

        redisTemplateMessage.expire(key, 5, TimeUnit.DAYS);

        chatRoomService.updateChatRoomLastMessage(
                chatMessage.getChatRoomId(), responseDTO.getCreatedAt());
    }

    @Override
    public ChatMessageResponseListDTO getMessageList(Long roomId) {

        List<ChatMessage> messageList = new ArrayList<>();
        String key = CHAT_ROOM + roomId;

        redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessage.class));
        List<ChatMessage> redisMessageList = redisTemplateMessage.opsForList().range(key, 0, 99);
        if (redisMessageList == null || redisMessageList.isEmpty()) {
            List<ChatMessage> dbMessageList =
                    chatMessageRepository.findTop100ByChatRoomIdOrderByCreatedAtDesc(roomId);

            for (int i = dbMessageList.size() - 1; i >= 0; i--) {
                ChatMessage chatMessage = dbMessageList.get(i);
                messageList.add(chatMessage);
                redisTemplateMessage.opsForList().leftPush(key, chatMessage);
            }
        } else {
            messageList.addAll(redisMessageList);
            messageList.sort(Comparator.comparing(ChatMessage::getCreatedAt).reversed());
        }

        return ChatMessageConverter.toChatMessageResponseListDTO(messageList);
    }

    @Override
    @Transactional
    public void sendRentCommonMessage(Member member, Long roomId, String message, ChatType type) {
        ChatMessageRequestDTO chatMessage =
                ChatMessageConverter.toRentMessage(member, roomId, message, type);
        publishMessage(chatMessage);
    }

    @Override
    @Transactional
    public void sendRentRequestMessage(
            Member member, Long roomId, Long rentId, String message, ChatType type) {

        ChatMessageRequestDTO chatMessage =
                ChatMessageConverter.toRentRequestMessage(member, roomId, rentId, message, type);

        publishMessage(chatMessage);
    }
}
