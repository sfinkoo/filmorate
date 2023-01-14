package filmorate.dao.impl;

import filmorate.dao.MpaDao;
import filmorate.exception.ResourceException;
import filmorate.models.Mpa;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
public class MpaDaoImpl implements MpaDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getMpa() {
        String sql = "SELECT * FROM MPA";
        return jdbcTemplate.query(sql, this::mapRowToMpa);
    }

    @Override
    public Mpa getMpaFromFilm(String idMpa) {
        String sql =
                "SELECT ID, NAME " +
                        "FROM MPA " +
                        "WHERE ID=" + idMpa;

        List<Mpa> mpa = jdbcTemplate.query(sql, this::mapRowToMpa);
        if (mpa.isEmpty()) {
            log.info("MPA с идентификатором {} не найден.", idMpa);
            throw new ResourceException(HttpStatus.NOT_FOUND, "Такого MPA нет в базе.");
        } else {
            return mpa.get(0);
        }
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
