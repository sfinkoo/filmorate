package filmorate.service;

import filmorate.dao.MpaDao;
import filmorate.models.Mpa;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {

    private final MpaDao mpaDao;

    public List<Mpa> getMpaService() {
        return mpaDao.getMpa();
    }

    public Mpa getMpaFromFilmService(String idFilm) {
        return mpaDao.getMpaFromFilm(idFilm);
    }
}
