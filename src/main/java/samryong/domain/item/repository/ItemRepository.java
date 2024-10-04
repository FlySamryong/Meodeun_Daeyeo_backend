package samryong.domain.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import samryong.domain.item.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {}
