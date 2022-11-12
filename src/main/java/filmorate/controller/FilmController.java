package filmorate.controller;

import filmorate.IdCreator;
import filmorate.exception.ResourceException;
import filmorate.exception.ValidationException;
import filmorate.models.Film;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final IdCreator idCreator = new IdCreator();
    private static final LocalDate MOVIE_BIRTHDAY = LocalDate.parse("1895-12-28");
    private final HashMap<Integer, Film> films = new HashMap<>();
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.debug("Количество фильмов до добавления: {}", films.size());
        if (validateName(film)) {
            log.debug("Переданы некорректные данные.");
            throw new ValidationException("Проверьте данные и сделайте повторный запрос.");
        }
        film.setId(idCreator.createId());
        films.put(film.getId(), film);
        log.debug("Количество фильмов после добавления: {}", films.size());
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (!validateContainsId(film)) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Фильм с таким id не найден.");
        }
        films.put(film.getId(), film);
        log.debug("Информация о фильме успешно обновлена.");
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    private boolean validateName(Film film) {
        return film.getName().equals("")
                || film.getReleaseDate().isBefore(MOVIE_BIRTHDAY);
    }

    private boolean validateContainsId(Film film) {
        return films.containsKey(film.getId());
    }
}