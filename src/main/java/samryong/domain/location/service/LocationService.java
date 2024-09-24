package samryong.domain.location.service;

import samryong.domain.location.dto.LocationDTO.LocationRequestDTO;
import samryong.domain.location.entity.Location;

public interface LocationService {

    Location getLocation(LocationRequestDTO requestDto);

    Long createLocation(LocationRequestDTO locationDTO);
}
