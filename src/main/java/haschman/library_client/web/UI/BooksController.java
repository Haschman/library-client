package haschman.library_client.web.UI;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/Books")
public class BooksController {
    @GetMapping
    public String index(Model model) {
        return "books";
    }
}
