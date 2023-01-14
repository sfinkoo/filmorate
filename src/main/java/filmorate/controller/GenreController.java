package filmorate.controller;

import filmorate.models.Genre;
import filmorate.service.GenreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping()
    public Set<Genre> getGenres() {
        log.debug("Получаем список всех жанров.");
        return genreService.getGenresService();
    }

    @GetMapping("/{id}")
    public Genre getGenresFilm(@PathVariable String id) {
        log.debug("Получаем жанр {}.", id);
        return genreService.getGenresFromFilmService(id);
    }
}
