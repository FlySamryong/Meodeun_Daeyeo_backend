package samryong.item.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import samryong.item.domain.Item_Category;

public interface Item_CategoryRepository extends JpaRepository<Item_Category, Long> {

    @Override
    Optional<Item_Category> findById(Long Id);
}
