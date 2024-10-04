package samryong.domain.redis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import samryong.domain.item.entity.Item;

public interface RecentItemRepository extends JpaRepository<Item, Long> { }