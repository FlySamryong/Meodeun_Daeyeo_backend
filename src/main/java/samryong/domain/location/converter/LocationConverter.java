package samryong.domain.location.converter;

import org.springframework.stereotype.Component;
import samryong.domain.location.dto.LocationDTO.LocationRequestDTO;
import samryong.domain.location.entity.Location;

@Component
public class LocationConverter {

    public static Location toLocation(LocationRequestDTO requestDTO) {
        return Location.builder()
                .city(requestDTO.getCity())
                .district(requestDTO.getDistrict())
                .neighborhood(requestDTO.getNeighborhood())
                .build();
    }
}
