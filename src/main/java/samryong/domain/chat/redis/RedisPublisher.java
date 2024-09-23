package samryong.domain.chat.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import samryong.domain.chat.dto.ChatMessageDTO.ChatMessageRequestDTO;

@Service
@RequiredArgsConstructor
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    // 채팅 메시지를 Redis 의 특정 Topic 으로 발행
    // 대기중인 RedisSubscriber 에게 메시지를 전달
    public void publish(ChannelTopic topic, ChatMessageRequestDTO requestDTO) {
        redisTemplate.convertAndSend(topic.getTopic(), requestDTO);
    }
}
