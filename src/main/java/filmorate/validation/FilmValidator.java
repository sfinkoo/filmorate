package filmorate.validation;

import filmorate.exception.ValidationException;
import filmorate.models.Film;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Component
public class FilmValidator {

    private static final LocalDate MOVIE_BIRTHDAY = LocalDate.parse("1895-12-28");

    public void validateName(Film film) throws ValidationException {
        if (film.getName().isBlank()
                || film.getReleaseDate().isBefore(MOVIE_BIRTHDAY)) {
            throw new ValidationException("Проверьте данные и сделайте повторный запрос.");
        }
    }
}
