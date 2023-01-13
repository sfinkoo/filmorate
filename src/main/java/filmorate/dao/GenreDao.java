package filmorate.dao;

import filmorate.models.Genre;

import java.util.Optional;
import java.util.Set;

public interface GenreDao {

    Set<Genre> getGenres();

    Optional<Genre> getGenresFromFilm(String idFilm);
}
