package haschman.library_client.web.UI.controller;

import haschman.library_client.web.UI.model.BookModel;
import haschman.library_client.web.domain.BookDTO;
import haschman.library_client.web.service.BookService;
import jakarta.ws.rs.ClientErrorException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/Books")
public class BooksController {
    private final BookService bookService;

    public BooksController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String listAll(Model model) {
        try {
            model.addAttribute("books", bookService.readAll());
        } catch (ClientErrorException e) {
            model.addAttribute("error", true);
            model.addAttribute("errormsg", e.getMessage());
        }

        return "books";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("bookModel", new BookModel());
        return "newBook";
    }

    @PostMapping("/newSubmit")
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

    @GetMapping("/detail")
    public String detail(@RequestParam("id") Long id, Model model) {
        bookService.setCurrentBook(id);
        model.addAttribute("books", bookService.readOne().orElseThrow());
        return "bookDetail";
    }
}
