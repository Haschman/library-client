package haschman.library_client.web.service;

import haschman.library_client.web.api.AuthorClient;
import haschman.library_client.web.domain.AuthorDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AuthorService {
    private AuthorClient authorClient;
    private boolean currentAuthorSet;

    public AuthorService(AuthorClient authorClient) {
        this.authorClient = authorClient;
    }

    public AuthorDTO create(AuthorDTO authorDTO) {
        return authorClient.create(authorDTO);
    }

    public List<AuthorDTO> readAll() {
        return authorClient.readAll();
    }

    public void setCurrentAuthor(Long id) {
        currentAuthorSet = true;
        authorClient.setCurrentAuthor(id);
    }

    public Optional<AuthorDTO> readOne() {
        return authorClient.readOne();
    }

    public void deleteOne() {
        currentAuthorSet = false;
        authorClient.deleteOne();
    }
}
