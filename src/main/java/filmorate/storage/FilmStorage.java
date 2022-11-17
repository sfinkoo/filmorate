package filmorate.storage;

import filmorate.models.Film;
import filmorate.models.User;

import java.util.List;

public interface FilmStorage {

    void addFilm(Film film);

    void updateFilm(Film film);

    List<Film> getAllFilms();

    List<Integer> getAllIDs();

    Film getFilmById(int id);

    void addLike(int idFilm, User user);

    void deleteLike(int idFilm, User user);
}
