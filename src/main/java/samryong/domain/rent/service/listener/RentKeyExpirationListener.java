package samryong.domain.rent.service.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import samryong.domain.notice.service.NoticeService;
import samryong.domain.rent.service.RentExpirationService;

@Component
public class RentKeyExpirationListener extends KeyExpirationEventMessageListener {

    private static final String RENT = "RENT:";
    private static final String NOTICE = "NOTICE:";

    @Autowired private RentExpirationService rentExpirationService;
    @Autowired private NoticeService noticeService;

    public RentKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();

        if (expiredKey.startsWith(RENT)) {
            rentExpirationService.processRentKeyExpiration(expiredKey);
        }
        if (expiredKey.startsWith(NOTICE)) {
            noticeService.tradeRemind(Long.parseLong(expiredKey.substring(NOTICE.length())));
        }
    }
}
