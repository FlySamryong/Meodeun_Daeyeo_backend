package samryong.domain.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import samryong.domain.item.entity.WishItem;

public interface WishItemRepository extends JpaRepository<WishItem, Long> {}
