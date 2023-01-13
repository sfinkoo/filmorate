package filmorate.storage;

import filmorate.models.Film;
import filmorate.models.User;
import filmorate.service.IdCreator;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

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
//        films.get(idFilm).getLikes().put(user.getId(), user);
    }

    @Override
    public void deleteLike(int idFilm, User user) {
//        films.get(idFilm).getLikes().remove(user.getId());
    }

    @Override
    public List<Film> getTopsFilms(Integer count) {
//        List<Film> topsFilmReverse = getAllFilms().stream()
//                .sorted(Comparator.<Film>comparingInt(film -> film.getLikes().size()).reversed())
//                .limit(count)
//                .collect(Collectors.toList());
//        Collections.reverse(topsFilmReverse);
//        return topsFilmReverse;
        return null;
    }
}
