package samryong.domain.chat.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import samryong.domain.chat.dto.ChatMessageDTO.ChatMessageRequestDTO;
import samryong.global.code.GlobalErrorCode;
import samryong.global.exception.GlobalException;

@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // redis 에서 발행된 데이터를 받아 역직렬화
            String publishMessage =
                    (String) redisTemplate.getStringSerializer().deserialize(message.getBody());

            ChatMessageRequestDTO requestDTO =
                    objectMapper.readValue(publishMessage, ChatMessageRequestDTO.class);

            messagingTemplate.convertAndSend(
                    "/topic/chat/room/" + requestDTO.getChatRoomId(), requestDTO); // 구독 대상으로 메시지 발송
        } catch (Exception e) {
            throw new GlobalException(GlobalErrorCode.CHAT_INTERNAL_ERROR);
        }
    }
}
