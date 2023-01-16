package filmorate.service;

import filmorate.dao.GenreDao;
import filmorate.models.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreDao genreDao;

    public Set<Genre> getGenresService() {
        return genreDao.getGenres();
    }

    public Genre getGenresFromFilmService(String igFilm) {
        return genreDao.getGenresFromFilm(igFilm);
    }
}
