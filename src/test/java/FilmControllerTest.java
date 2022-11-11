import filmorate.IdCreator;
import filmorate.controller.FilmController;
import filmorate.exception.ValidationException;
import filmorate.models.Film;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {

    private final IdCreator idCreator = new IdCreator() {
        int id = 1;

        @Override
        public int createId() {
            return id++;
        }
    };

    private static final FilmController FILM_CONTROLLER = new FilmController();
    private final Film film = new Film(idCreator.createId(), "Король лев",
            "Король лев, описание", "1995-01-20", 200);

    @Test
    public void addFilmTest() throws ValidationException {
        FILM_CONTROLLER.addFilm(film);
        assertTrue(FILM_CONTROLLER.getAllFilms().contains(film));
    }

    @Test
    public void addFilmTestWithNotCorrectDataTest() {
        Film film = new Film(idCreator.createId(), "",
                "Король лев, описание", "1995-01-20", 200);
        Assertions.assertThrows(ValidationException.class, () -> FILM_CONTROLLER.addFilm(film));
    }

    @Test
    public void updateFilmTest() throws ValidationException {
        Film addFilm = FILM_CONTROLLER.addFilm(film);
        Film filmForUpdate = new Film(addFilm.getId(), "Король лев",
                "Король лев, описание", "1995-01-20", 87);
        FILM_CONTROLLER.updateFilm(filmForUpdate);
        assertEquals(FILM_CONTROLLER.getAllFilms().get(addFilm.getId() - 1), filmForUpdate);
    }

    @Test
    public void getAllFilmsTest() {
        assertNotNull(FILM_CONTROLLER.getAllFilms());
    }
}
