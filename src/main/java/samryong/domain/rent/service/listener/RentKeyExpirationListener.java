package samryong.domain.rent.service.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import samryong.domain.rent.service.RentExpirationService;

@Component
public class RentKeyExpirationListener extends KeyExpirationEventMessageListener {

    private static final String RENT = "RENT:";

    @Autowired private RentExpirationService rentExpirationService;

    public RentKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();

        if (expiredKey.startsWith(RENT)) {
            rentExpirationService.processRentKeyExpiration(expiredKey);
        }
    }
}
