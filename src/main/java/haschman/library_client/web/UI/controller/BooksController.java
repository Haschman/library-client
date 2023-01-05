package haschman.library_client.web.UI.controller;

import haschman.library_client.web.UI.model.BookModel;
import haschman.library_client.web.domain.AuthorDTO;
import haschman.library_client.web.domain.BookDTO;
import haschman.library_client.web.domain.LocationDTO;
import haschman.library_client.web.service.AuthorService;
import haschman.library_client.web.service.BookService;
import haschman.library_client.web.service.LocationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/Books")
public class BooksController {
    private final BookService bookService;
    private final AuthorService authorService;
    private final LocationService locationService;

    public BooksController(BookService bookService, AuthorService authorService, LocationService locationService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.locationService = locationService;
    }

    @GetMapping
    public String listAll(Model model) {
        List<BookDTO> books = bookService.readAll();
        books.sort(Comparator.comparing(BookDTO::getName));
        model.addAttribute("books", books);
        Map<Long, AuthorDTO> authorDTOMap = authorService.readAll().stream().collect(Collectors.toMap(AuthorDTO::getId, Function.identity()));
        model.addAttribute("authors", authorDTOMap);
        Map<Long, LocationDTO> locationDTOMap = locationService.readAll().stream().collect(Collectors.toMap(LocationDTO::getId, Function.identity()));
        model.addAttribute("locations", locationDTOMap);
        model.addAttribute("deleted", false);
        model.addAttribute("deleteError", false);
        model.addAttribute("message", "");
        model.addAttribute("filterError", false);
        return "books";
    }

    @GetMapping("/start-with")
    public String listFiltered(@RequestParam("character") Character character, Model model) {
        try {
            List<BookDTO> books = bookService.filterBooks(character);
            books.sort(Comparator.comparing(BookDTO::getName));
            model.addAttribute("books", books);
            Map<Long, AuthorDTO> authorDTOMap = authorService.readAll().stream().collect(Collectors.toMap(AuthorDTO::getId, Function.identity()));
            model.addAttribute("authors", authorDTOMap);
            Map<Long, LocationDTO> locationDTOMap = locationService.readAll().stream().collect(Collectors.toMap(LocationDTO::getId, Function.identity()));
            model.addAttribute("locations", locationDTOMap);
            model.addAttribute("deleted", false);
            model.addAttribute("deleteError", false);
            model.addAttribute("filterError", false);
            model.addAttribute("message", "");
            return "books";
        } catch (RuntimeException e) {
            List<BookDTO> books = bookService.readAll();
            books.sort(Comparator.comparing(BookDTO::getName));
            model.addAttribute("books", books);
            Map<Long, AuthorDTO> authorDTOMap = authorService.readAll().stream().collect(Collectors.toMap(AuthorDTO::getId, Function.identity()));
            model.addAttribute("authors", authorDTOMap);
            Map<Long, LocationDTO> locationDTOMap = locationService.readAll().stream().collect(Collectors.toMap(LocationDTO::getId, Function.identity()));
            model.addAttribute("locations", locationDTOMap);
            model.addAttribute("deleted", false);
            model.addAttribute("deleteError", false);
            model.addAttribute("filterError", true);
            model.addAttribute("message", e.getMessage());
            return "books";
        }
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("bookModel", new BookModel());
        model.addAttribute("allAuthors", authorService.readAll());
        model.addAttribute("allLocations", locationService.readAll());
        return "newBook";
    }

    @PostMapping("/new")
    public String createSubmit(@ModelAttribute BookModel bookModel, Model model) {
        try {
            BookDTO bookDTO = bookService.create(bookModel);
            model.addAttribute("success", true);
            model.addAttribute("successMessage", "Book successfully created with id: " + bookDTO.id);
            model.addAttribute("bookModel", bookDTO);
            model.addAttribute("allAuthors", authorService.readAll());
            model.addAttribute("allLocations", locationService.readAll());
        } catch (Exception e) {
            model.addAttribute("success", false);
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("bookModel", bookModel);
            model.addAttribute("allAuthors", authorService.readAll());
            model.addAttribute("allLocations", locationService.readAll());
        }
        return "newBook";
    }

    @GetMapping("/edit")
    public String update(@RequestParam("id") Long id, Model model) {
        bookService.setCurrentBook(id);
        Optional<BookDTO> book = bookService.readOne();
        if (book.isPresent()) {
            model.addAttribute("bookDTO", book.get());
            model.addAttribute("allAuthors", authorService.readAll());
            model.addAttribute("allLocations", locationService.readAll());
            model.addAttribute("findingError", false);
        } else {
            model.addAttribute("findingError", true);
        }
        return "editBook";
    }

    @PostMapping("/edit")
    public String editSubmit(@ModelAttribute BookDTO bookDTO, Model model) {
        try {
            bookService.setCurrentBook(bookDTO.getId());
            bookService.update(bookDTO);
            model.addAttribute("success", true);
            model.addAttribute("successMessage", "Book successfully updated");
            model.addAttribute("bookDTO", bookDTO);
            model.addAttribute("allAuthors", authorService.readAll());
            model.addAttribute("allLocations", locationService.readAll());
            model.addAttribute("findingError", false);
        } catch (Exception e) {
            model.addAttribute("success", false);
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("bookDTO", bookDTO);
            model.addAttribute("allAuthors", authorService.readAll());
            model.addAttribute("allLocations", locationService.readAll());
            model.addAttribute("findingError", false);
        }
        return "editBook";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam("id") Long id, Model model) {
        bookService.setCurrentBook(id);
        Optional<BookDTO> currentBook = bookService.readOne();
        if (currentBook.isPresent()){
            model.addAttribute("book", currentBook.get());

            // Get all authors
            Set<AuthorDTO> authorsOfBook = new HashSet<>();
            for (Long authorID : currentBook.get().authors) { // Goes through all ID's of authors of current book
                authorService.setCurrentAuthor(authorID);
                Optional<AuthorDTO> oneAuthorOfBook = authorService.readOne();
                // Adds author to set for model
                oneAuthorOfBook.ifPresent(authorsOfBook::add);
            }
            model.addAttribute("authors", authorsOfBook);
            Map<Long, LocationDTO> locationDTOMap = locationService.readAll().stream().collect(Collectors.toMap(LocationDTO::getId, Function.identity()));
            model.addAttribute("locations", locationDTOMap);
            model.addAttribute("findingError", false);
        } else
            model.addAttribute("findingError", true);
        return "bookDetail";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam Long id, Model model) {
        bookService.setCurrentBook(id);
        if (! bookService.deleteOne()) {
            model.addAttribute("deleted", false);
            model.addAttribute("deleteError", true);
            model.addAttribute("message", "Error occurred when deleting book with ID: " + id);
        } else {
            model.addAttribute("deleted", true);
            model.addAttribute("deleteError", false);
            model.addAttribute("message", "Successfully deleted book with ID: " + id);
        }

        Collection<BookDTO> books = bookService.readAll();
        model.addAttribute("books", books);
        Map<Long, AuthorDTO> authorDTOMap = authorService.readAll().stream().collect(Collectors.toMap(AuthorDTO::getId, Function.identity()));
        model.addAttribute("authors", authorDTOMap);
        Map<Long, LocationDTO> locationDTOMap = locationService.readAll().stream().collect(Collectors.toMap(LocationDTO::getId, Function.identity()));
        model.addAttribute("locations", locationDTOMap);
        model.addAttribute("filterError", false);
        return "books";
    }
}
