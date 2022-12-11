package haschman.library_client.web.service;

import haschman.library_client.web.api.BookClient;
import haschman.library_client.web.domain.BookDTO;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class BookService {
    private BookClient bookClient;

    public BookService(BookClient bookClient) {
        this.bookClient = bookClient;
    }

    public Collection<BookDTO> readAll() {
        return bookClient.readAll();
    }
}
