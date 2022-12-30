package haschman.library_client.web.service;

import haschman.library_client.web.api.BookClient;
import haschman.library_client.web.domain.BookDTO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

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

    public List<BookDTO> readAll() {
        return bookClient.readAll();
    }

    public void setCurrentBook(Long id) {
        currentBookSet = true;
        bookClient.setCurrentBook(id);
    }

    public Optional<BookDTO> readOne() {
        return bookClient.readOne();
    }

    public void update(BookDTO bookDTO) {
        bookClient.update(bookDTO);
    }

    public boolean deleteOne() {
        if (bookClient.deleteOne()) {
            currentBookSet = false;
            return true;
        }
        currentBookSet = false;
        return false;
    }

    public List<BookDTO> readBooksByAuthor(Long authorID) {
        return bookClient.readBooksByAuthor(authorID);
    }
}
