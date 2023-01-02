package haschman.library_client.web.service;

import haschman.library_client.web.api.BookClient;
import haschman.library_client.web.domain.BookDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookService {
    private final BookClient bookClient;

    public BookService(BookClient bookClient) {
        this.bookClient = bookClient;
    }

    public BookDTO create(BookDTO bookDTO) {
        return bookClient.create(bookDTO);
    }

    public List<BookDTO> readAll() {
        return bookClient.readAll();
    }

    public void setCurrentBook(Long id) {
        bookClient.setCurrentBook(id);
    }

    public Optional<BookDTO> readOne() {
        return bookClient.readOne();
    }

    public void update(BookDTO bookDTO) {
        bookClient.update(bookDTO);
    }

    public boolean deleteOne() {
        return bookClient.deleteOne();
    }

    public List<BookDTO> readBooksByAuthor(Long authorID) {
        return bookClient.readBooksByAuthor(authorID);
    }

    public List<BookDTO> readBooksFromLocation(Long locationID) {
        return bookClient.readBooksFromLocation(locationID);
    }
}
