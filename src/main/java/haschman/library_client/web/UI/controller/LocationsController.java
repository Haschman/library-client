package haschman.library_client.web.UI.controller;

import haschman.library_client.web.UI.model.LocationModel;
import haschman.library_client.web.domain.AuthorDTO;
import haschman.library_client.web.domain.BookDTO;
import haschman.library_client.web.domain.LocationDTO;
import haschman.library_client.web.service.BookService;
import haschman.library_client.web.service.LocationService;
import jakarta.ws.rs.BadRequestException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/Locations")
public class LocationsController {
    private final LocationService locationService;
    private final BookService bookService;

    public LocationsController(LocationService locationService, BookService bookService) {
        this.locationService = locationService;
        this.bookService = bookService;
    }

    @GetMapping
    public String listAll(Model model) {
        List<LocationDTO> locations = locationService.readAll();
        model.addAttribute("locations", locations);
        model.addAttribute("deleted", false);
        model.addAttribute("deleteError", false);
        model.addAttribute("message", "");
        return "locations";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("locationModel", new LocationModel());
        return "newLocation";
    }

    @PostMapping("/new")
    public String createSubmit(@ModelAttribute LocationModel locationModel, Model model) {
        try {
            LocationDTO locationDTO = locationService.crate(locationModel);
            model.addAttribute("success", true);
            model.addAttribute("successMessage", "Location successfully created with id: " + locationDTO.id);
            model.addAttribute("locationModel", locationModel);
        } catch (Exception e) {
            model.addAttribute("success", false);
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("locationModel", locationModel);
        }
        return "newLocation";
    }

    @GetMapping("/edit")
    public String update(@RequestParam("id") Long id, Model model) {
        locationService.setCurrentLocation(id);
        Optional<LocationDTO> location = locationService.readOne();
        if (location.isPresent()) {
            model.addAttribute("locationDTO", location.get());
            model.addAttribute("findingError", false);
        } else
            model.addAttribute("findingError", true);
        return "editLocation";
    }

    @PostMapping("/edit")
    public String editSubmit(@ModelAttribute LocationDTO locationDTO, Model model) {
        try {
            locationService.setCurrentLocation(locationDTO.getId());
            locationService.update(locationDTO);
            model.addAttribute("success", true);
            model.addAttribute("successMessage", "Location successfully updated");
            model.addAttribute("locationDTO", locationDTO);
            model.addAttribute("findingError", false);
        } catch (Exception e) {
            model.addAttribute("success", false);
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("locationDTO", locationDTO);
            model.addAttribute("findingError", false);
        }
        return "editLocation";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam("id") Long id, Model model) {
        locationService.setCurrentLocation(id);
        Optional<LocationDTO> currentLocation = locationService.readOne();
        if (currentLocation.isPresent()){
            model.addAttribute("location", currentLocation.get());
            model.addAttribute("books", bookService.readBooksFromLocation(id));
            model.addAttribute("findingError", false);
        } else
            model.addAttribute("findingError", true);
        return "locationDetail";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam Long id, Model model) {
        locationService.setCurrentLocation(id);
        try {
            locationService.deleteOne();
            model.addAttribute("deleted", true);
            model.addAttribute("deleteError", false);
            model.addAttribute("message", "Successfully deleted location with ID: " + id);
        } catch (BadRequestException e) {
            model.addAttribute("deleted", false);
            model.addAttribute("deleteError", true);
            StringBuilder message = new StringBuilder(e.getMessage());
            List<BookDTO> writtenBooks = bookService.readBooksFromLocation(id);
            for (BookDTO book : writtenBooks)
                message.append("<br>").append(book.getName());
            model.addAttribute("message", message.toString());
        } catch (Exception e) {
            model.addAttribute("deleted", false);
            model.addAttribute("deleteError", true);
            model.addAttribute("message", e.getMessage());
        }
        List<LocationDTO> locations = locationService.readAll();
        model.addAttribute("locations", locations);
        return "locations";
    }
}
