package filmorate.models;

import filmorate.exception.ResourceException;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.TreeSet;

@Data
@Builder
public class Film {

    private int id;
    private String name;
    @Size(max = 200)
    private String description;

    private LocalDate releaseDate;
    @Positive
    @Min(value = 0)
    private int duration;

    private String rate;
    private Mpa mpa;
    private TreeSet<Genre> genres;
}
