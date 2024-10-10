package samryong.domain.redis.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import samryong.domain.item.converter.ItemConverter;
import samryong.domain.item.dto.ItemDTO.RecentItemResponseListDTO;
import samryong.domain.item.entity.Item;
import samryong.domain.item.repository.ItemRepository;
import samryong.domain.member.entity.Member;
import samryong.global.code.GlobalErrorCode;
import samryong.global.exception.GlobalException;

@Service
@RequiredArgsConstructor
public class RecentItemServiceImpl implements RecentItemService {

    private final RedisTemplate<String, Long> redisTemplate;

    private static final String RECENT_ITEM_KEY = "recent:item:";
    private static final long EXPIRATION = 60 * 60 * 24; // 24시간
    private static final int MAX_RECENT_ITEMS = 20; // 최대 20개 저장
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public void saveRecentItem(Member member, Long itemId) {
        String key = getRecentItemKey(member.getId());

        redisTemplate.opsForList().remove(key, 0, itemId); // 중복된 것 삭제
        redisTemplate.opsForList().leftPush(key, itemId);
        redisTemplate.opsForList().trim(key, 0, MAX_RECENT_ITEMS - 1);

        redisTemplate.expire(key, EXPIRATION, TimeUnit.SECONDS);
    }

    @Override
    public RecentItemResponseListDTO getRecentItemList(Member member) {

        String key = getRecentItemKey(member.getId());

        List<Item> itemList =
                Optional.ofNullable(redisTemplate.opsForList().range(key, 0, -1)).orElse(List.of()).stream()
                        .map(
                                itemId ->
                                        itemRepository
                                                .findById(itemId)
                                                .orElseThrow(() -> new GlobalException(GlobalErrorCode.ITEM_NOT_FOUND)))
                        .toList();

        return ItemConverter.toRecentItemResponseListDTO(itemList);
    }

    private String getRecentItemKey(Long memberId) {
        return RECENT_ITEM_KEY + memberId;
    }
}
