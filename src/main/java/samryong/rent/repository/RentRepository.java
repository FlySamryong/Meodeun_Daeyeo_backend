package samryong.rent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import samryong.rent.domain.Rent;

@Repository
public interface RentRepository extends JpaRepository<Rent, Long> {}
