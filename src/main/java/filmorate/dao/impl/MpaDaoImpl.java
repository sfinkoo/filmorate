package filmorate.dao.impl;

import filmorate.dao.MpaDao;
import filmorate.exception.ResourceException;
import filmorate.models.Mpa;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        List<Mpa> mpas = new ArrayList<>();
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM MPA");
        while (mpaRows.next()) {
            Mpa mpa = Mpa.builder()
                    .id(mpaRows.getInt("id"))
                    .name(mpaRows.getString("name"))
                    .build();
            log.info("Найден рейтинг: {} {}", mpa.getId(), mpa.getName());
            mpas.add(mpa);
        }
        log.info("Вернули список MPA");
        return mpas;
    }

    @Override
    public Optional<Mpa> getMpaFromFilm(String idMpa) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(
                "SELECT ID, NAME " +
                        "FROM MPA " +
                        "WHERE ID=?", idMpa);
        if (mpaRows.next()) {
            Mpa mpa = Mpa.builder()
                    .id(mpaRows.getInt("id"))
                    .name(mpaRows.getString("name"))
                    .build();
            log.info("Найден MPA: {} {}", mpa.getId(), mpa.getName());
            return Optional.of(mpa);
        } else {
            log.info("MPA с идентификатором {} не найден.", idMpa);
            throw new ResourceException(HttpStatus.NOT_FOUND, "Такого MPA нет в базе.");
        }
    }
}
