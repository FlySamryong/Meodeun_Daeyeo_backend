package samryong.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import samryong.item.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {}
