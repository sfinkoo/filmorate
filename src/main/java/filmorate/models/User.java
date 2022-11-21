package filmorate.models;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private int id;

    @Email
    @NotNull
    private String email;

    @NotBlank
    @NotNull
    private String login;

    private String name;
    @PastOrPresent()
    private LocalDate birthday;

    private Set<Integer> friends;

    public User(int id, String email, String login, String name, String birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = (name == null || name.isBlank()) ? login : name;
        this.birthday = LocalDate.parse(birthday, DateTimeFormatter.ISO_DATE);
        this.friends = new HashSet<>();
    }
}

