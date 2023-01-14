package filmorate.dao.impl;

import filmorate.exception.ResourceException;
import filmorate.exception.ValidationException;
import filmorate.models.Film;
import filmorate.models.Genre;
import filmorate.models.Mpa;
import filmorate.models.User;
import filmorate.storage.FilmStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

@Slf4j
@Component("filmDao")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFilm(Film film) throws ValidationException {
        checkNameFilm(film);
        String sql = "INSERT INTO FILM (NAME, DESCRIPTION, RELEASEDATE, DURATION, RATE) "
                + "VALUES ((?), (?), (?), (?), (?))";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate());

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from FILM where NAME=?", film.getName());
        if (filmRows.next()) {
            film.setId(filmRows.getInt("id"));
        }

        if (film.getMpa() != null) {
            String sqlForMpa = "INSERT INTO FILM_MPA (FILM_ID, MPA_ID) VALUES ((?), (?))";
            jdbcTemplate.update(sqlForMpa,
                    film.getId(),
                    film.getMpa().getId());
        }

        if (film.getGenres() != null) {
            String sqlForGenre = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES ((?), (?))";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sqlForGenre,
                        film.getId(),
                        genre.getId());
            }

        }
    }

    @Override
    public void updateFilm(Film film) throws ValidationException {
        checkId(film.getId());
        checkNameFilm(film);
        String sql = "UPDATE FILM SET NAME=?, DESCRIPTION=?, RELEASEDATE=?, DURATION=?, RATE=?"
                + " WHERE ID = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getId());

        if (film.getGenres() != null) {
            if (!film.getGenres().isEmpty()) {
                String sqlGD = "DELETE FROM FILM_GENRE WHERE FILM_ID=?";
                jdbcTemplate.update(sqlGD, film.getId());
                String sqlG = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES ((?), (?))";
                for (Genre genre : film.getGenres()) {
                    jdbcTemplate.update(sqlG, film.getId(), genre.getId());
                }
            } else {
                String sqlG = "DELETE FROM FILM_GENRE WHERE FILM_ID=?";
                jdbcTemplate.update(sqlG, film.getId());
            }
        }

        if (film.getMpa() != null) {
            if (film.getMpa().getId() > 0) {
                String sqlForMpa = "DELETE FROM FILM_MPA WHERE FILM_ID=?";
                jdbcTemplate.update(sqlForMpa, film.getId());
                String sqlForMpaInsert = "INSERT INTO FILM_MPA (FILM_ID, MPA_ID) VALUES ((?), (?))";
                jdbcTemplate.update(sqlForMpaInsert, film.getId(), film.getMpa().getId());
            } else {
                String sqlForMpa = "DELETE FROM FILM_MPA WHERE FILM_ID=?";
                jdbcTemplate.update(sqlForMpa, film.getId());
            }
        }
    }

    @Override
    public List<Film> getAllFilms() {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from FILM");
        return getFilms(filmRows);
    }

    @Override
    public Film getFilmById(int id) {
        try {
            String sqlQuery = "select * from FILM where ID= " + id;
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm);
        } catch (DataAccessException dataAccessException) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Пользователь с таким id не найден.");
        }
    }

    @Override
    public void addLike(int idFilm, User user) {
        String sql = "INSERT INTO LIKES(USER_ID, FILM_ID) VALUES ((?), (?))";
        jdbcTemplate.update(sql, user.getId(), idFilm);
    }

    @Override
    public void deleteLike(int idFilm, User user) throws ValidationException {
        checkId(idFilm);
        String sql = "DELETE FROM LIKES WHERE USER_ID=? AND FILM_ID=?";
        jdbcTemplate.update(sql, user.getId(), idFilm);
    }

    @Override
    public List<Film> getTopsFilms(Integer count) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(
                "SELECT F.ID, F.NAME, F.DESCRIPTION, F.RELEASEDATE, F.DURATION, F.RATE "
                        + "FROM FILM F LEFT JOIN LIKES L on F.ID = L.FILM_ID GROUP BY F.ID "
                        + "ORDER BY COUNT(L.USER_ID) DESC LIMIT ?", count);
        return getFilms(filmRows);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("id"))
                .description(resultSet.getString("description"))
                .duration(resultSet.getInt("duration"))
                .rate(resultSet.getString("rate"))
                .name(resultSet.getString("name"))
                .releaseDate(resultSet.getDate("releasedate").toLocalDate())
                .genres(getGenresForFilm(resultSet.getInt("id")))
                .mpa(getMpaForFilm(resultSet.getInt("ID")))
                .build();
    }

    private void checkNameFilm(Film film) throws ValidationException {
        LocalDate MOVIE_BIRTHDAY = LocalDate.parse("1895-12-28");
        if (film.getName().isBlank() || film.getName().isEmpty()
                || film.getReleaseDate().isBefore(MOVIE_BIRTHDAY)) {
            throw new ValidationException("Фильм не соответствует условиям. " +
                    "Проверьте данные и повторите запрос.");
        }
    }

    private void checkId(int id) throws ValidationException {
        if (getFilmById(id) == null) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Фильм с таким id не найден.");
        } else if (id < 0) {
            throw new ValidationException("Отрицательные значения не допустимы.");
        }
    }

    private List<Film> getFilms(SqlRowSet filmRows) {
        List<Film> films = new ArrayList<>();
        while (filmRows.next()) {
            Film film = Film.builder()
                    .id(filmRows.getInt("ID"))
                    .name(Objects.requireNonNull(filmRows.getString("NAME")))
                    .description(Objects.requireNonNull(filmRows.getString("DESCRIPTION")))
                    .releaseDate(Objects.requireNonNull(filmRows.getDate("RELEASEDATE")).toLocalDate())
                    .duration(filmRows.getInt("DURATION"))
                    .rate(filmRows.getString("RATE"))
                    .mpa(getMpaForFilm(filmRows.getInt("ID")))
                    .genres(getGenresForFilm(filmRows.getInt("ID")))
                    .build();
            log.info("Найден фильм: {} {}", film.getId(), film.getName());

            films.add(film);
        }
        return films;
    }

    private Mpa getMpaForFilm(int idFilm) {
        String sql =
                "SELECT M2.ID, M2.NAME "
                        + "FROM FILM F "
                        + "LEFT JOIN FILM_MPA FM on F.ID = FM.film_id "
                        + "LEFT JOIN MPA M2 on FM.mpa_id = M2.ID "
                        + "WHERE F.ID =" + idFilm;

        RowMapper<Mpa> rowMapper = (rs, rowNum) ->
                Mpa.builder()
                        .id(rs.getInt(1))
                        .name(rs.getString(2))
                        .build();

        List<Mpa> mpa = jdbcTemplate.query(sql, rowMapper);

        if (mpa.isEmpty()) {
            throw new ResourceException(HttpStatus.OK, "у фильма пока нет MPA.");
        } else {
            return mpa.get(0);
        }
    }

    private TreeSet<Genre> getGenresForFilm(int idFilm) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(
                "SELECT G2.ID, G2.NAME "
                        + "FROM FILM F "
                        + "LEFT JOIN FILM_GENRE FG on F.ID = FG.film_id "
                        + "LEFT JOIN GENRE G2 on FG.genre_id = G2.ID "
                        + "WHERE F.ID =?", idFilm);
        TreeSet<Genre> genresSet = new TreeSet<>();
        while (genreRows.next()) {
            Genre genre = Genre.builder()
                    .id(genreRows.getInt("id"))
                    .name(genreRows.getString("name"))
                    .build();
            if (genre.getId() > 0) {
                genresSet.add(genre);
            }
        }
        return genresSet;
    }

    private TreeSet<Genre> checkGenreToNull(TreeSet<Genre> genres) {
        boolean check = true;
        if (genres != null) {
            if (!genres.isEmpty()) {
                for (Genre genre : genres) {
                    check = genre.getId() > 0;
                }
                if (!check) {
                    genres.clear();
                }
            }
        }
        return genres;
    }
}


