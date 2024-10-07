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
    @Transactional
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

        redisTemplateMessage.expire(chatMessage.getChatRoomId().toString(), 30, TimeUnit.MINUTES);

        chatRoomService.updateChatRoomLastMessage(chatMessage.getChatRoomId());
    }

    @Override
    public ChatMessageResponseListDTO getMessageList(Long roomId) {

        List<ChatMessage> messageList = new ArrayList<>();

        redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessage.class));
        List<ChatMessage> redisMessageList =
                redisTemplateMessage.opsForList().range(roomId.toString(), 0, 99);
        if (redisMessageList == null || redisMessageList.isEmpty()) {
            List<ChatMessage> dbMessageList =
                    chatMessageRepository.findTop100ByChatRoomIdOrderByCreatedAtDesc(roomId);

            for (ChatMessage chatMessage : dbMessageList) {

                messageList.add(chatMessage);
                redisTemplateMessage
                        .opsForList()
                        .leftPush(chatMessage.getChatRoomId().toString(), chatMessage);
            }
        } else {
            messageList.addAll(redisMessageList);
            messageList.sort(Comparator.comparing(ChatMessage::getCreatedAt).reversed());
        }

        return ChatMessageConverter.toChatMessageResponseListDTO(messageList);
    }
}
