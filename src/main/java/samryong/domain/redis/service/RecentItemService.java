package samryong.domain.redis.service;

import java.util.List;
import samryong.domain.member.entity.Member;

public interface RecentItemService {

    void saveRecentItem(Member member, Long itemId);

    List<Object> getRecentItems(Member member);
}
