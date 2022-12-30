package haschman.library_client.web.UI.controller;

import haschman.library_client.web.domain.AuthorDTO;
import haschman.library_client.web.service.AuthorService;
import jakarta.ws.rs.ClientErrorException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/Authors")
public class AuthorsController {
    private final AuthorService authorService;

    public AuthorsController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public String listAll(Model model) {
        try {
            List<AuthorDTO> authors = authorService.readAll();
            authors.sort(Comparator.comparing(AuthorDTO::getSurname));
            model.addAttribute("authors", authors);
            model.addAttribute("error", false);
        } catch (ClientErrorException e) {
            // TODO: This is not used
            model.addAttribute("error", true);
            model.addAttribute("errorMsg", e.getMessage());
        }
        return "authors";
    }
}
