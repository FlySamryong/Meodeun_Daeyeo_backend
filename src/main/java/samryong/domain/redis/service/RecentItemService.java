package samryong.domain.redis.service;

import samryong.domain.member.entity.Member;

import java.util.List;

public interface RecentItemService {

    public void saveRecentItem(Member member, Long itemId);

    public List<Object> getRecentItems(Member member);
}
