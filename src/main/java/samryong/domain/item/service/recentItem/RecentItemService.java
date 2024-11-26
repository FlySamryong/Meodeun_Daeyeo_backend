package samryong.domain.item.service.recentItem;

import samryong.domain.item.dto.ItemDTO.RecentItemResponseListDTO;
import samryong.domain.member.entity.Member;

public interface RecentItemService {

    void saveRecentItem(Member member, Long itemId);

    RecentItemResponseListDTO getRecentItemList(Member member);
}
