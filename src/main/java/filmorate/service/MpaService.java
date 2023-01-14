package filmorate.service;

import filmorate.dao.MpaDao;
import filmorate.models.Mpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MpaService {

    private final MpaDao mpaDao;

    @Autowired
    public MpaService(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    public List<Mpa> getMpaService() {
        return mpaDao.getMpa();
    }

    public Mpa getMpaFromFilmService(String idFilm) {
        return mpaDao.getMpaFromFilm(idFilm);
    }
}
