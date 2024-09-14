package samryong.item.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import samryong.item.domain.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Override
    Optional<Item> findById(Long Id);
}
