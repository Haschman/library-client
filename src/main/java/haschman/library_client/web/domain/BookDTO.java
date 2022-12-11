package haschman.library_client.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookDTO {
    public Long id;
    public String name;
    public String language;
    public Long ISBN;
    public Integer publication_year;
    public String category;
    public String genre;
    //public Pair<Integer, Integer> location;
    public final Set<AuthorDTO> authors = new HashSet<>();
    public String authorName;
    public String authorSurname;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Long getISBN() {
        return ISBN;
    }

    public void setISBN(Long ISBN) {
        this.ISBN = ISBN;
    }

    public Integer getPublication_year() {
        return publication_year;
    }

    public void setPublication_year(Integer publication_dateS) {
        this.publication_year = publication_dateS;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

//    public Pair<Integer, Integer> getLocation() {
//        return location;
//    }
//
//    public void setLocation(int stand, int shelf) {
//        this.location = Pair.of(stand, shelf);
//    }

    public Set<AuthorDTO> getAuthors() {
        return authors;
    }

    public void addAuthor(AuthorDTO author) {
        authors.add(author);
    }
}
