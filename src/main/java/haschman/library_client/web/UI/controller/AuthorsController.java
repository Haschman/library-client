package haschman.library_client.web.UI.controller;

import haschman.library_client.web.UI.model.AuthorModel;
import haschman.library_client.web.domain.AuthorDTO;
import haschman.library_client.web.domain.BookDTO;
import haschman.library_client.web.service.AuthorService;
import haschman.library_client.web.service.BookService;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ClientErrorException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/Authors")
public class AuthorsController {
    private final AuthorService authorService;
    private final BookService bookService;

    public AuthorsController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @GetMapping
    public String listAll(Model model) {
        try {
            List<AuthorDTO> authors = authorService.readAll();
            authors.sort(Comparator.comparing(AuthorDTO::getSurname));
            model.addAttribute("authors", authors);
            model.addAttribute("deleted", false);
            model.addAttribute("deleteError", false);
            model.addAttribute("message", "");
        } catch (ClientErrorException e) {
            // TODO: This is not used
            model.addAttribute("error", true);
            model.addAttribute("errorMsg", e.getMessage());
        }
        return "authors";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("authorModel", new AuthorModel());
        return "newAuthor";
    }

    @PostMapping("/new")
    public String createSubmit(@ModelAttribute AuthorModel authorModel, Model model) {
        try {
            AuthorDTO authorDTO = authorService.create(authorModel);
            model.addAttribute("success", true);
            model.addAttribute("successMessage", "Author successfully created with id: " + authorDTO.id);
            model.addAttribute("authorModel", authorDTO);
        } catch (Exception e) {
            model.addAttribute("success", false);
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("authorModel", authorModel);
        }
        return "newAuthor";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam("id") Long id, Model model) {
        authorService.setCurrentAuthor(id);
        Optional<AuthorDTO> currentAuthor = authorService.readOne();
        if (currentAuthor.isPresent()){
            model.addAttribute("author", currentAuthor.get());
            model.addAttribute("books", bookService.readBooksByAuthor(id));
            model.addAttribute("findingError", false);
        } else
            model.addAttribute("findingError", true);
        return "authorDetail";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam Long id, Model model) {
        authorService.setCurrentAuthor(id);
        try {
            authorService.deleteOne();
            model.addAttribute("deleted", true);
            model.addAttribute("deleteError", false);
            model.addAttribute("message", "Successfully deleted author with ID: " + id);
        } catch (BadRequestException e) {
            model.addAttribute("deleted", false);
            model.addAttribute("deleteError", true);
            String message = e.getMessage();
            List<BookDTO> writtenBooks = bookService.readBooksByAuthor(id);
            for (BookDTO book : writtenBooks)
                message = message + "<br>" + book.getName();
            model.addAttribute("message", message);
        } catch (Exception e) {
            model.addAttribute("deleted", false);
            model.addAttribute("deleteError", true);
            model.addAttribute("message", e.getMessage());
        }
        List<AuthorDTO> authors = authorService.readAll();
        authors.sort(Comparator.comparing(AuthorDTO::getSurname));
        model.addAttribute("authors", authors);
        return "authors";
    }
}
