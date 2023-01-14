package filmorate.controller;

import filmorate.exception.ValidationException;
import filmorate.models.Film;
import filmorate.service.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        return filmService.updateFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @DeleteMapping("/{id}")
    public void deleteFilmById(@PathVariable int id) throws ValidationException {
        filmService.deleteFilmById(id);
    }

    @DeleteMapping
    public void deleteAllFilms() {
        filmService.deleteAllFilm();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        return filmService.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLIke(@PathVariable("id") int idFilm, @PathVariable int userId) {
        filmService.addLIke(idFilm, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") int idFilm, @PathVariable int userId) throws ValidationException {
        filmService.deleteLike(idFilm, userId);
    }

    @GetMapping("/popular")
    public List<Film> getTopsFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        return filmService.getTopsFilms(count);
    }
}