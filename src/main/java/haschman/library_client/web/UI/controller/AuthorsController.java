package haschman.library_client.web.UI.controller;

import haschman.library_client.web.UI.model.AuthorModel;
import haschman.library_client.web.domain.AuthorDTO;
import haschman.library_client.web.service.AuthorService;
import jakarta.ws.rs.ClientErrorException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
}
