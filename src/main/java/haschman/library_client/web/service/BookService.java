package haschman.library_client.web.service;

import haschman.library_client.web.api.BookClient;
import haschman.library_client.web.domain.BookDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

    public List<BookDTO> filterBooks(Character character) {
        if (! Character.isLetter(character))
            throw new RuntimeException("Book must be filtered with a letter");
        character = Character.toUpperCase(character);
        List<BookDTO> books = readAll();
        List<BookDTO> filteredBooks = new ArrayList<>();
        for (BookDTO book : books) {
            if (Character.toUpperCase(book.name.charAt(0)) == character)
                filteredBooks.add(book);
        }
        return filteredBooks;
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
