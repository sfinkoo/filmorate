package filmorate.dao;

import filmorate.models.Mpa;

import java.util.List;

public interface MpaDao {

    List<Mpa> getMpa();

    Mpa getMpaFromFilm(String idFilm);
}
