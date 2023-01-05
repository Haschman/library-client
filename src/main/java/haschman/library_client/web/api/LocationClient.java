package haschman.library_client.web.api;

import haschman.library_client.web.domain.LocationDTO;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class LocationClient {
    private final WebTarget allLocationURL;
    private final WebTarget singleLocationTemplate;
    private WebTarget singleLocationURL;

    public LocationClient(@Value("${server.url}") String apiURL) {
        var client = ClientBuilder.newClient();
        allLocationURL = client.target(apiURL + "/locations");
        singleLocationTemplate = allLocationURL.path("/{id}");
    }

    public LocationDTO create(LocationDTO locationDTO) {
        LocationDTO createdLocation = new LocationDTO();
        try {
            createdLocation = allLocationURL.request(MediaType.APPLICATION_JSON_TYPE)
                        .post(Entity.entity(locationDTO, MediaType.APPLICATION_JSON_TYPE), LocationDTO.class);
        } catch (ClientErrorException e) {
            var response = e.getResponse();
            var responseBody = response.readEntity(new GenericType<Map<String, String>>(){});
            throw new RuntimeException(responseBody.get("message"));
        }
        return createdLocation;
    }

    public List<LocationDTO> readAll() {
        var locations = allLocationURL.request(MediaType.APPLICATION_JSON_TYPE).get(LocationDTO[].class);
        return Arrays.asList(locations);
    }

    public void setCurrentLocation(Long id) {
        singleLocationURL = singleLocationTemplate.resolveTemplate("id", id);
    }

    public Optional<LocationDTO> readOne() {
        var response = singleLocationURL.request(MediaType.APPLICATION_JSON_TYPE).get();
        if (response.getStatus() == 200)
            return Optional.of(response.readEntity(LocationDTO.class));
        if (response.getStatus() == 404)
            return Optional.empty();
        throw new RuntimeException(response.getStatusInfo().getReasonPhrase());
    }

    public void update(LocationDTO locationDTO) {
        var response = singleLocationURL.request(MediaType.APPLICATION_JSON_TYPE).put(Entity.entity(locationDTO, MediaType.APPLICATION_JSON_TYPE));
        if (response.getStatus() == 400) {
            var responseBody = response.readEntity(new GenericType<Map<String, String>>() {});
            throw new RuntimeException(responseBody.get("message"));
        }
        if (response.getStatus() == 404) {
            var responseBody = response.readEntity(new GenericType<Map<String, String>>() {});
            throw new RuntimeException(responseBody.get("message"));
        }
        if (response.getStatus() == 409) {
            var responseBody = response.readEntity(new GenericType<Map<String, String>>() {});
            throw new RuntimeException(responseBody.get("message"));
        }
    }

    public void deleteOne() {
        Response response = singleLocationURL.request(MediaType.APPLICATION_JSON_TYPE).delete();
        if (response.getStatus() == 404) {
            var responseBody = response.readEntity(new GenericType<Map<String, String>>(){});
            throw new RuntimeException(responseBody.get("message"));
        }
        if (response.getStatus() == 400) {
            var responseBody = response.readEntity(new GenericType<Map<String, String>>(){});
            throw new BadRequestException(responseBody.get("message").replace("something", "location"));
        }
    }
}
