package filmorate.service;

import filmorate.exception.ResourceException;
import filmorate.exception.ValidationException;
import filmorate.models.Film;
import filmorate.storage.FilmStorage;
import filmorate.storage.UserStorage;
import filmorate.validation.FilmValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final FilmValidator filmValidator;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmValidator filmValidator, @Qualifier("filmDao") FilmStorage filmStorage, @Qualifier("userDao") UserStorage userStorage) {
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
        log.debug("Лайк успешно поставлен.");
        filmStorage.addLike(idFilm, userStorage.getUserById(userId));
    }

    public void deleteLike(int idFilm, int userId) {
        validateContainsIdUser(userId);
        log.debug("Лайк успешно удален.");
        filmStorage.deleteLike(idFilm, userStorage.getUserById(userId));
    }

    public List<Film> getTopsFilms(Integer count) {
        List<Film> topsFilmReverse = filmStorage.getAllFilms().stream()
                .sorted(Comparator.<Film>comparingInt(film -> film.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
        Collections.reverse(topsFilmReverse);
        return topsFilmReverse;
    }

    private void validateContainsId(int id) {
        if (filmStorage.getFilmById(id) == null) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Фильм с таким id не найден.");
        }
    }

    private void validateContainsIdUser(int id) {
        if (userStorage.getUserById(id) == null) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Пользователь с таким id не найден.");
        }
    }
}
