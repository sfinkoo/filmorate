package filmorate.dao.impl;

import filmorate.dao.GenreDao;
import filmorate.exception.ResourceException;
import filmorate.models.Genre;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Set<Genre> getGenres() {
        String sql = ("SELECT * FROM GENRE");
        return new TreeSet<>(jdbcTemplate.query(sql, this::mapRowToGenre));
    }

    @Override
    public Genre getGenresFromFilm(String idGenre) {
        String sql =
                "SELECT *\n" +
                        "FROM GENRE\n" +
                        "WHERE ID=" + idGenre;
        List<Genre> genres = jdbcTemplate.query(sql, this::mapRowToGenre);

        if (genres.isEmpty()) {
            log.info("жанр с идентификатором {} не найден.", idGenre);
            throw new ResourceException(HttpStatus.NOT_FOUND, "Такого жанра нет в базе.");
        } else {
            return genres.get(0);
        }
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
