package filmorate.dao.impl;

import filmorate.dao.GenreDao;
import filmorate.exception.ResourceException;
import filmorate.models.Genre;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
@Component
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Set<Genre> getGenres() {
        Set<Genre> genres = new TreeSet<>();
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM GENRE");
        while (genreRows.next()) {
            Genre genre = Genre.builder()
                    .id(genreRows.getInt("id"))
                    .name(Objects.requireNonNull(genreRows.getString("name")))
                    .build();
            log.info("Найден жанр: {} {}", genre.getId(), genre.getName());
            if (genre.getId() > 0) {
                genres.add(genre);
            }
        }
        return genres;
    }

    @Override
    public Optional<Genre> getGenresFromFilm(String idGenre) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(
                "SELECT *\n" +
                        "FROM GENRE\n" +
                        "WHERE ID=?", idGenre);
        if (genreRows.next()) {
            Genre genre = Genre.builder()
                    .id(genreRows.getInt("id"))
                    .name(genreRows.getString("name"))
                    .build();
            log.info("Найден жанр: {} {}", genre.getId(), genre.getName());
            return Optional.of(genre);
        } else {
            log.info("жанр с идентификатором {} не найден.", idGenre);
            throw new ResourceException(HttpStatus.NOT_FOUND, "Такого жанра нет в базе.");
        }
    }
}
