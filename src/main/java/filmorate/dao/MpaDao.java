package filmorate.dao;

import filmorate.models.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaDao {

    List<Mpa> getMpa();

    Optional<Mpa> getMpaFromFilm(String idFilm);
}
