package filmorate.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.TreeSet;

@Data
@Builder
@AllArgsConstructor
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
