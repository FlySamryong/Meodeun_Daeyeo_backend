package samryong.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import samryong.location.domain.Location;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository <Location, Long> {
    Optional<Location> findById(long id);
}
