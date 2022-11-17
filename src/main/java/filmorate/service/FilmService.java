package filmorate.service;

import filmorate.exception.ResourceException;
import filmorate.exception.ValidationException;
import filmorate.models.Film;
import filmorate.storage.FilmStorage;
import filmorate.storage.UserStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmValidator filmValidator;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    private final static Logger log = LoggerFactory.getLogger(FilmService.class);

    @Autowired
    public FilmService(FilmValidator filmValidator, FilmStorage filmStorage, UserStorage userStorage) {
        this.filmValidator = filmValidator;
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    public Film addFilm(Film film) throws ValidationException {
        filmValidator.validateName(film);
        filmStorage.addFilm(film);
        log.debug("Фильм успешно добавлен.");
        return film;
    }

    public Film updateFilm(Film film) {
        validateContainsId(film.getId());
        filmStorage.updateFilm(film);
        log.debug("Информация о фильме успешно обновлена.");
        return film;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(int id) {
        validateContainsId(id);
        return filmStorage.getFilmById(id);
    }

    public void addLIke(int idFilm, int userId) {
        filmStorage.addLike(idFilm, userStorage.getUserByID(userId));
    }

    public void deleteLike(int idFilm, int userId) {
        validateContainsIdUser(userId);
        filmStorage.deleteLike(idFilm, userStorage.getUserByID(userId));
    }

    public List<Film> getTopsFilms(Integer count) {
        List<Film> topsFilmReverse = filmStorage.getAllFilms().stream()
                .sorted(Comparator.<Film>comparingInt(film -> film.getLikes().size()).reversed())
                .limit(count).collect(Collectors.toList());
        Collections.reverse(topsFilmReverse);
        return topsFilmReverse;
    }

    private void validateContainsId(int id) {
        if (filmStorage.getFilmById(id) == null) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Фильм с таким id не найден.");
        }
    }

    private void validateContainsIdUser(int id) {
        if (userStorage.getUserByID(id) == null) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Пользователь с таким id не найден.");
        }
    }
}
