package haschman.library_client.web.service;

import haschman.library_client.web.api.BookClient;
import haschman.library_client.web.domain.BookDTO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Component
public class BookService {
    private BookClient bookClient;
    private boolean currentBookSet;

    public BookService(BookClient bookClient) {
        this.bookClient = bookClient;
    }

    public BookDTO create(BookDTO bookDTO) {
        return bookClient.create(bookDTO);
    }

    public Collection<BookDTO> readAll() {
        return bookClient.readAll();
    }

    public void setCurrentBook(Long id) {
        currentBookSet = true;
        bookClient.setCurrentBook(id);
    }

    public Optional<BookDTO> readOne() {
        return bookClient.readOne();
    }
}
