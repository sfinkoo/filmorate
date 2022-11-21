package filmorate.storage;

import filmorate.models.Film;
import filmorate.models.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final IdCreator idCreator = new IdCreator();
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public void addFilm(Film film) {
        film.setId(idCreator.createId());
        films.put(film.getId(), film);
    }

    @Override
    public void updateFilm(Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(int id) {
        return films.get(id);
    }

    @Override
    public void addLike(int idFilm, User user) {
        films.get(idFilm).getLikes().put(user.getId(), user);
    }

    @Override
    public void deleteLike(int idFilm, User user) {
        films.get(idFilm).getLikes().remove(user.getId());
    }
}
