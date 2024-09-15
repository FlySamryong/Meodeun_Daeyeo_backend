package samryong.domain.rent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import samryong.domain.rent.entity.Rent;

@Repository
public interface RentRepository extends JpaRepository<Rent, Long> {}
