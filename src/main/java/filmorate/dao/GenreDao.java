package filmorate.dao;

import filmorate.models.Genre;

import java.util.Set;

public interface GenreDao {

    Set<Genre> getGenres();

    Genre getGenresFromFilm(String idFilm);
}
