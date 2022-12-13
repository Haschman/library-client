package haschman.library_client.web.service;

import haschman.library_client.web.api.BookClient;
import haschman.library_client.web.domain.BookDTO;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class BookService {
    private BookClient bookClient;
    private boolean currentBookSet;

    public BookService(BookClient bookClient) {
        this.bookClient = bookClient;
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
