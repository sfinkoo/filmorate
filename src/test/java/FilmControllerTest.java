import filmorate.controller.FilmController;
import filmorate.exception.ValidationException;
import filmorate.models.Film;
import filmorate.service.FilmService;
import filmorate.service.FilmValidator;
import filmorate.service.UserService;
import filmorate.storage.IdCreator;
import filmorate.storage.InMemoryFilmStorage;
import filmorate.storage.InMemoryUserStorage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = {
        FilmController.class,
        FilmService.class,
        FilmValidator.class,
        InMemoryFilmStorage.class,
        UserService.class,
        InMemoryUserStorage.class
})
public class FilmControllerTest {

    @Autowired
    private FilmController filmController;
    private final IdCreator idCreator = new IdCreator();

    private final Film film = new Film(idCreator.createId(), "Король лев",
            "Король лев, описание", "1995-01-20", 200);

    @Test
    public void addFilmTest() throws ValidationException {
        filmController.addFilm(film);
        assertTrue(filmController.getAllFilms().contains(film));
    }

    @Test
    public void addFilmTestWithNotCorrectDataTest() {
        Film film = new Film(idCreator.createId(), "",
                "Король лев, описание", "1995-01-20", 200);
        Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void updateFilmTest() throws ValidationException {
        Film addFilm = filmController.addFilm(film);
        Film filmForUpdate = new Film(addFilm.getId(), "Король лев",
                "Король лев, описание", "1995-01-20", 87);
        filmController.updateFilm(filmForUpdate);
        assertEquals(filmController.getAllFilms().get(addFilm.getId() - 1), filmForUpdate);
    }

    @Test
    public void getAllFilmsTest() {
        assertNotNull(filmController.getAllFilms());
    }
}
