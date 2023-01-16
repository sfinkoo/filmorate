package filmorate.service;

import filmorate.exception.ValidationException;
import filmorate.models.Film;
import filmorate.storage.FilmStorage;
import filmorate.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmDao;
    private final UserStorage userDao;

    public Film addFilm(Film film) throws ValidationException {
        filmDao.addFilm(film);
        log.debug("Фильм успешно добавлен.");
        return film;
    }

    public Film updateFilm(Film film) throws ValidationException {
        filmDao.updateFilm(film);
        log.debug("Информация о фильме успешно обновлена.");
        return film;
    }

    public List<Film> getAllFilms() {
        return filmDao.getAllFilms();
    }

    public Film getFilmById(int id) {
        return filmDao.getFilmById(id);
    }

    public void addLIke(int idFilm, int userId) {
        log.debug("Лайк успешно поставлен.");
        filmDao.addLike(idFilm, userDao.getUserById(userId));
    }

    public void deleteLike(int idFilm, int userId) throws ValidationException {
        log.debug("Лайк успешно удален.");
        filmDao.deleteLike(idFilm, userDao.getUserById(userId));
    }

    public List<Film> getTopsFilms(Integer count) {
        return filmDao.getTopsFilms(count);
    }

    public void deleteFilmById(int id) throws ValidationException {
        filmDao.deleteFilmById(id);
    }

    public void deleteAllFilm() {
        filmDao.deleteAllFilms();
    }
}
