package haschman.library_client.web.UI;

import haschman.library_client.web.service.BookService;
import jakarta.ws.rs.ClientErrorException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


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
}
