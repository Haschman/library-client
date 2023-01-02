package haschman.library_client.web.api;

import haschman.library_client.web.domain.AuthorDTO;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AuthorClient {
    private WebTarget allAuthorsURL;
    private WebTarget singleURLTemplate;
    private WebTarget singleAuthorURL;

    public AuthorClient(@Value("${server.url}") String apiURL) {
        var client = ClientBuilder.newClient();
        allAuthorsURL = client.target(apiURL + "/authors");
        singleURLTemplate = allAuthorsURL.path("/{id}");
    }

    public AuthorDTO create(AuthorDTO authorDTO) {
        return allAuthorsURL.request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(authorDTO, MediaType.APPLICATION_JSON_TYPE), AuthorDTO.class);
    }

    public List<AuthorDTO> readAll() {
        var authors = allAuthorsURL.request(MediaType.APPLICATION_JSON_TYPE).get(AuthorDTO[].class);
        return Arrays.asList(authors);
    }

    public void setCurrentAuthor(Long id) {
        singleAuthorURL = singleURLTemplate.resolveTemplate("id", id);
    }

    public Optional<AuthorDTO> readOne() {
        var response = singleAuthorURL.request(MediaType.APPLICATION_JSON_TYPE).get();
        if (response.getStatus() == 200)
            return Optional.of(response.readEntity(AuthorDTO.class));
        if (response.getStatus() == 404)
            return Optional.empty();
        throw new RuntimeException(response.getStatusInfo().getReasonPhrase());
    }

    public void update(AuthorDTO authorDTO) {
        var response = singleAuthorURL.request(MediaType.APPLICATION_JSON_TYPE).put(Entity.entity(authorDTO, MediaType.APPLICATION_JSON_TYPE));
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
        Response response = singleAuthorURL.request(MediaType.APPLICATION_JSON_TYPE).delete();
        if (response.getStatus() == 404) {
            var responseBody = response.readEntity(new GenericType<Map<String, String>>(){});
            throw new RuntimeException(responseBody.get("message"));
        }
        if (response.getStatus() == 400) {
            var responseBody = response.readEntity(new GenericType<Map<String, String>>(){});
            throw new BadRequestException(responseBody.get("message"));
        }
    }
}
