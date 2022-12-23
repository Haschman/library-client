package haschman.library_client.web.api;

import haschman.library_client.web.domain.BookDTO;
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
public class BookClient {
    private WebTarget allBooksURL;
    private WebTarget singleURLTemplate;
    private WebTarget singleBookURL;

    public BookClient(@Value("${server.url}") String apiURL) {
        var client = ClientBuilder.newClient();
        allBooksURL = client.target(apiURL + "/books");
        singleURLTemplate = allBooksURL.path("/{id}");
    }

    public BookDTO create(BookDTO bookDTO) {
        return allBooksURL.request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(bookDTO, MediaType.APPLICATION_JSON_TYPE), BookDTO.class);
    }

    public Collection<BookDTO> readAll() {
        var books = allBooksURL.request(MediaType.APPLICATION_JSON_TYPE).get(BookDTO[].class);
        return Arrays.asList(books);
    }

    public void setCurrentBook(Long id) {
        singleBookURL = singleURLTemplate.resolveTemplate("id", id);
    }

    public Optional<BookDTO> readOne() {
        var response = singleBookURL.request(MediaType.APPLICATION_JSON_TYPE).get();
        if (response.getStatus() == 200)
            return Optional.of(response.readEntity(BookDTO.class));
        if (response.getStatus() == 404)
            return Optional.empty();
        throw new RuntimeException(response.getStatusInfo().getReasonPhrase());
    }

    public void deleteOne() {
        singleBookURL.request(MediaType.APPLICATION_JSON_TYPE).delete();
    }
}


