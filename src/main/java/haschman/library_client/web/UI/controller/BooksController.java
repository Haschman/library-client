package haschman.library_client.web.UI.controller;

import haschman.library_client.web.UI.model.BookModel;
import haschman.library_client.web.domain.AuthorDTO;
import haschman.library_client.web.domain.BookDTO;
import haschman.library_client.web.service.AuthorService;
import haschman.library_client.web.service.BookService;
import jakarta.ws.rs.ClientErrorException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/Books")
public class BooksController {
    private final BookService bookService;
    private final AuthorService authorService;

    public BooksController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @GetMapping
    public String listAll(Model model) {
        try {
            Collection<BookDTO> books = bookService.readAll();
            model.addAttribute("books", books);
            Map<Long, AuthorDTO> authorDTOMap = authorService.readAll().stream().collect(Collectors.toMap(AuthorDTO::getId, Function.identity()));
            model.addAttribute("authors", authorDTOMap);
            model.addAttribute("deleted", false);
            model.addAttribute("deleteError", false);
            model.addAttribute("message", "");
        } catch (ClientErrorException e) {
            model.addAttribute("error", true);
            model.addAttribute("errorMsg", e.getMessage());
        }

        return "books";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("bookModel", new BookModel());
        model.addAttribute("allAuthors", authorService.readAll());
        return "newBook";
    }

    @PostMapping("/new")
    public String createSubmit(@ModelAttribute BookModel bookModel, Model model) {
        try {
            BookDTO bookDTO = bookService.create(bookModel);
            model.addAttribute("success", true);
            model.addAttribute("successMessage", "Book successfully created with id: " + bookDTO.id);
            model.addAttribute("bookModel", bookDTO);
        } catch (Exception e) {
            model.addAttribute("success", false);
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("bookModel", bookModel);
        }
        return "newBook";
    }

    @GetMapping("/edit")
    public String update(@RequestParam("id") Long id, Model model) {
        bookService.setCurrentBook(id);
        model.addAttribute("bookDTO", bookService.readOne().orElseThrow());
        model.addAttribute("allAuthors", authorService.readAll());
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
        } catch (Exception e) {
            model.addAttribute("success", false);
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("bookModel", bookDTO);
            model.addAttribute("allAuthors", authorService.readAll());
        }
        return "editBook";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam("id") Long id, Model model) {
        // Get book
        bookService.setCurrentBook(id);
        Optional<BookDTO> currentBook = bookService.readOne();
        model.addAttribute("book", bookService.readOne().orElseThrow());
        // TODO: Make something if currentBook.isEmpty() - display page that this book does not exists

        // Get all authors
        Set<AuthorDTO> authorsOfBook = new HashSet<>();
        if (currentBook.isPresent()) {
            for (Long authorID : currentBook.get().authors) { // Goes through all ID's of authors of current book
                authorService.setCurrentAuthor(authorID);
                Optional<AuthorDTO> oneAuthorOfBook = authorService.readOne();
                if (oneAuthorOfBook.isPresent())
                    authorsOfBook.add(oneAuthorOfBook.get()); // Adds author to set for model
            }
        }
        model.addAttribute("authors", authorsOfBook);

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
        return "books";
    }
}
