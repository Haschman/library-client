package haschman.library_client.web.api;

import haschman.library_client.web.domain.AuthorDTO;
import haschman.library_client.web.domain.BookDTO;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Component
public class BookClient {
    private WebTarget allBooksURL;
    private WebTarget singleURLTemplate;
    private WebTarget singleBookURL;
    private WebTarget allBooksByAuthorURLTemplate;
    private WebTarget allBooksByAuthorURL;

    public BookClient(@Value("${server.url}") String apiURL) {
        var client = ClientBuilder.newClient();
        allBooksURL = client.target(apiURL + "/books");
        singleURLTemplate = allBooksURL.path("/{id}");
        allBooksByAuthorURLTemplate = allBooksURL.path("/author").queryParam("authorID", "{authorID}");
    }

    public BookDTO create(BookDTO bookDTO) {
        BookDTO createdBook = new BookDTO();
        try {
            createdBook = allBooksURL.request(MediaType.APPLICATION_JSON_TYPE)
                    .post(Entity.entity(bookDTO, MediaType.APPLICATION_JSON_TYPE), BookDTO.class);
        } catch (ClientErrorException e) {
            var response = e.getResponse();
            var responseBody = response.readEntity(new GenericType<Map<String, String>>(){});
            throw new RuntimeException(responseBody.get("message"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return createdBook;
    }

    public List<BookDTO> readAll() {
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

    public void update(BookDTO bookDTO) {
        var response = singleBookURL.request(MediaType.APPLICATION_JSON_TYPE).put(Entity.entity(bookDTO, MediaType.APPLICATION_JSON_TYPE));
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

    public boolean deleteOne() {
        return singleBookURL.request(MediaType.APPLICATION_JSON_TYPE).delete().getStatus() == 200;
    }

    public List<BookDTO> readBooksByAuthor(Long authorID) {
        allBooksByAuthorURL = allBooksByAuthorURLTemplate.resolveTemplate("authorID", authorID);
        var response = allBooksByAuthorURL.request(MediaType.APPLICATION_JSON_TYPE).get();
        return response.readEntity(new GenericType<>(){});
    }
}


