package filmorate.service;

import filmorate.exception.ValidationException;
import filmorate.models.Film;
import filmorate.storage.FilmStorage;
import filmorate.storage.UserStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final IdCreator idCreator = new IdCreator();

    @Autowired
    public FilmService(@Qualifier("filmDao") FilmStorage filmStorage, @Qualifier("userDao") UserStorage userStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    public Film addFilm(Film film) throws ValidationException {
        film.setId(idCreator.createId());
        filmStorage.addFilm(film);
        log.debug("Фильм успешно добавлен.");
        return film;
    }

    public Film updateFilm(Film film) throws ValidationException {
        filmStorage.updateFilm(film);
        log.debug("Информация о фильме успешно обновлена.");
        return film;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    public void addLIke(int idFilm, int userId) {
        log.debug("Лайк успешно поставлен.");
        filmStorage.addLike(idFilm, userStorage.getUserById(userId));
    }

    public void deleteLike(int idFilm, int userId) throws ValidationException {
        log.debug("Лайк успешно удален.");
        filmStorage.deleteLike(idFilm, userStorage.getUserById(userId));
    }

    public List<Film> getTopsFilms(Integer count) {
        return filmStorage.getTopsFilms(count);
    }
}
