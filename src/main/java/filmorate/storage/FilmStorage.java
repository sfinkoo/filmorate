package filmorate.storage;

import filmorate.exception.ValidationException;
import filmorate.models.Film;
import filmorate.models.User;

import java.util.List;

public interface FilmStorage {

    void addFilm(Film film) throws ValidationException;

    void updateFilm(Film film) throws ValidationException;

    List<Film> getAllFilms();

    Film getFilmById(int id);

    void addLike(int idFilm, User user);

    void deleteLike(int idFilm, User user) throws ValidationException;

    List<Film> getTopsFilms(Integer count);
}
