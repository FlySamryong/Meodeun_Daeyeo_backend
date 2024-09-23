package samryong.domain.chat.service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;
import samryong.domain.chat.converter.ChatMessageConverter;
import samryong.domain.chat.dto.ChatMessageDTO.ChatMessageRequestDTO;
import samryong.domain.chat.dto.ChatMessageDTO.ChatMessageResponseListDTO;
import samryong.domain.chat.entity.ChatMessage;
import samryong.domain.chat.redis.RedisPublisher;
import samryong.domain.chat.repository.ChatMessageRepository;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final RedisPublisher redisPublisher;
    private final ChatRoomService chatRoomService;
    private final RedisTemplate<String, ChatMessage> redisTemplateMessage;

    @Override
    public void publishMessage(ChatMessageRequestDTO requestDTO) {

        chatRoomService.enterChatRoom(requestDTO.getChatRoomId()); // 리스너와 연동

        redisPublisher.publish(chatRoomService.getTopic(requestDTO.getChatRoomId()), requestDTO);

        saveMessage(requestDTO);
    }

    @Override
    @Transactional
    public void saveMessage(ChatMessageRequestDTO requestDTO) {
        ChatMessage chatMessage = ChatMessageConverter.toChatMessage(requestDTO);

        chatMessageRepository.save(chatMessage);

        redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessage.class));

        redisTemplateMessage.opsForList().leftPush(chatMessage.getChatRoomId().toString(), chatMessage);

        redisTemplateMessage.expire(chatMessage.getChatRoomId().toString(), 1, TimeUnit.MINUTES);
    }

    public ChatMessageResponseListDTO getMessageList(Long roomId) {

        List<ChatMessage> messageList = new ArrayList<>();

        List<ChatMessage> redisMessageList =
                redisTemplateMessage.opsForList().range(roomId.toString(), 0, 99);
        if (redisMessageList == null || redisMessageList.isEmpty()) {
            List<ChatMessage> dbMessageList =
                    chatMessageRepository.findTop100ByChatRoomIdOrderByCreatedAtDesc(roomId);

            for (ChatMessage chatMessage : dbMessageList) {

                messageList.add(chatMessage);
                redisTemplateMessage.setValueSerializer(
                        new Jackson2JsonRedisSerializer<>(ChatMessage.class));
                redisTemplateMessage
                        .opsForList()
                        .leftPush(chatMessage.getChatRoomId().toString(), chatMessage);
            }
        } else {
            messageList.addAll(redisMessageList);
        }

        return ChatMessageConverter.toChatMessageResponseListDTO(messageList);
    }
}
