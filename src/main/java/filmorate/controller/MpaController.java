package filmorate.controller;

import filmorate.models.Mpa;
import filmorate.service.MpaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/mpa")
public class MpaController {

    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping()
    public List<Mpa> getMpa() {
        log.debug("Получаем список рейтингов");
        return mpaService.getMpaService();
    }

    @GetMapping("/{id}")
    public Optional<Mpa> getMpaFromFilmService(@PathVariable String id) {
        log.debug("Получаем рейтинг у фильма с id {}", id);
        return mpaService.getMpaFromFilmService(id);
    }
}
