package samryong.domain.location.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import samryong.domain.location.converter.LocationConverter;
import samryong.domain.location.dto.LocationDTO.LocationRequestDTO;
import samryong.domain.location.entity.Location;
import samryong.domain.location.repository.LocationRepository;
import samryong.global.code.GlobalErrorCode;
import samryong.global.exception.GlobalException;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Override
    public Location getLocation(LocationRequestDTO requestDTO) {
        return locationRepository
                .findByCityAndDistrictAndNeighborhood(
                        requestDTO.getCity(), requestDTO.getDistrict(), requestDTO.getNeighborhood())
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.LOCATION_NOT_FOUND));
    }

    @Override
    public Long createLocation(LocationRequestDTO locationDTO) {
        Location location = LocationConverter.toLocation(locationDTO);
        locationRepository.save(location);
        return location.getId();
    }
}
