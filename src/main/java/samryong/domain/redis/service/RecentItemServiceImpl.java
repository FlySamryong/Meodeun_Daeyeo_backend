package samryong.domain.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import samryong.domain.member.entity.Member;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RecentItemServiceImpl implements RecentItemService{

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private static final String RECENT_ITEM_KEY = "recent:item:";
    private static final long EXPIRATION = 60 * 60 * 24;  // 24시간
    private static final int MAX_RECENT_ITEMS = 20; // 최대 20개 저장


    @Override
    @Transactional
    public void saveRecentItem(Member member, Long itemId) {
        String key = RECENT_ITEM_KEY + member.getId();
        redisTemplate.opsForList().remove(key, 0, itemId); // 중복된 것 삭제
        redisTemplate.opsForList().leftPush(key, itemId);
        redisTemplate.opsForList().trim(key, 0, MAX_RECENT_ITEMS-1);
        redisTemplate.expire(key, EXPIRATION, TimeUnit.SECONDS);
    }

    @Override
    @Transactional
    public List<Object> getRecentItems(Member member) {
        String key = RECENT_ITEM_KEY + member.getId();
        return redisTemplate.opsForList().range(key, 0, -1);
    }
}
