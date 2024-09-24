package samryong.domain.location.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import samryong.domain.location.entity.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findById(long id);

    Optional<Location> findByCityAndDistrictAndNeighborhood(
            String city, String district, String neighborhood);
}
