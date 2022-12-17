package haschman.library_client.web.api;

import haschman.library_client.web.domain.AuthorDTO;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

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

    public Collection<AuthorDTO> readAll() {
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
}
