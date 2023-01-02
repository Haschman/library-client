package haschman.library_client.web.service;

import haschman.library_client.web.api.LocationClient;
import haschman.library_client.web.domain.LocationDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class LocationService {
    private final LocationClient locationClient;

    public LocationService(LocationClient locationClient) {
        this.locationClient = locationClient;
    }

    public LocationDTO crate(LocationDTO locationDTO) {
        return locationClient.create(locationDTO);
    }

    public List<LocationDTO> readAll() {
        return locationClient.readAll();
    }

    public void setCurrentLocation(Long id) {
        locationClient.setCurrentLocation(id);
    }

    public Optional<LocationDTO> readOne() {
        return locationClient.readOne();
    }

    public void update(LocationDTO locationDTO) {
        locationClient.update(locationDTO);
    }

    public void deleteOne() {
        locationClient.deleteOne();
    }
}
