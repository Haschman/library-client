package haschman.library_client.web.service;

import haschman.library_client.web.api.AuthorClient;
import haschman.library_client.web.domain.AuthorDTO;
import org.jvnet.hk2.annotations.Service;
import org.springframework.stereotype.Component;

import java.util.Collection;
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

    public Collection<AuthorDTO> readAll() {
        return authorClient.readAll();
    }

    public void setCurrentAuthor(Long id) {
        currentAuthorSet = true;
        authorClient.setCurrentAuthor(id);
    }

    public Optional<AuthorDTO> readOne() {
        return authorClient.readOne();
    }
}
