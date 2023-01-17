import filmorate.dao.impl.FilmDbStorage;
import filmorate.dao.impl.UserDbStorage;
import filmorate.exception.ResourceException;
import filmorate.exception.ValidationException;
import filmorate.models.Film;
import filmorate.models.Mpa;
import filmorate.models.User;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {
        FilmDbStorage.class,
        UserDbStorage.class
})
@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final UserDbStorage userDbStorage;
    private final FilmDbStorage filmDbStorage;
    private User user1;
    private User user2;
    private User user3;
    private Film film1;
    private Film film2;

    @BeforeEach
    public void initEach() throws ValidationException {
        user1 = User.builder()
                .name("user1")
                .login("login1")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();
        userDbStorage.addUser(user1);
        user2 = User.builder()
                .name("user2")
                .login("login2")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();
        userDbStorage.addUser(user2);
        user3 = User.builder()
                .name("user3")
                .login("login3")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();
        userDbStorage.addUser(user3);

        film1 = Film.builder()
                .id(1)
                .name("film1")
                .description("descr1")
                .releaseDate(LocalDate.parse("2020-10-10"))
                .duration(120)
                .rate("1")
                .mpa(Mpa.builder().id(1).name("G").build())
                .genres(new TreeSet<>())
                .build();
        filmDbStorage.addFilm(film1);
        film2 = Film.builder()
                .id(1)
                .name("film2")
                .description("descr2")
                .releaseDate(LocalDate.parse("2020-10-10"))
                .duration(120)
                .rate("1")
                .mpa(Mpa.builder().id(1).name("G").build())
                .genres(new TreeSet<>())
                .build();
        filmDbStorage.addFilm(film2);
    }

    @AfterEach
    public void clearEach() {
        filmDbStorage.deleteAllFilms();
        userDbStorage.deleteAllUsers();
    }

    @Test
    public void isFindUserById() {
        assertDoesNotThrow(() -> userDbStorage.getUserById(user1.getId()));
    }

    @Test
    void isGetUsers() {
        List<User> expectedList = new ArrayList<>();
        expectedList.add(user1);
        expectedList.add(user2);
        expectedList.add(user3);

        List<User> actualListUsers = userDbStorage.getAllUsers();

        assertEquals(expectedList, actualListUsers, "Списки пользователей не совпадают");
    }

    @Test
    void isUpdateUser() {
        User expectedUpdateUser = User.builder()
                .id(1)
                .name("user2")
                .login("login2")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();
        userDbStorage.addUser(expectedUpdateUser);

        User actualUser = userDbStorage.updateUser(expectedUpdateUser);

        assertEquals(expectedUpdateUser, actualUser, "Пользователи не совпадают");
    }

    @Test
    public void isAddFriends() {
        userDbStorage.addToFriends(user1.getId(), user2.getId());
        List<User> actual = userDbStorage.getFriendsById(user1.getId());

        assertEquals(1, actual.size(), "Списки друзей не совпадают");
    }

    @Test
    void isGetListGeneralFriends() {
        userDbStorage.addToFriends(user1.getId(), user2.getId());
        userDbStorage.addToFriends(user3.getId(), user2.getId());
        List<User> actual = userDbStorage.getGeneralListFriends(user1.getId(), user3.getId());

        assertEquals(1, actual.size(), "Списки друзей не совпадают");
    }

    @Test
    public void isDeleteUserById() {
        userDbStorage.deleteUserById(user1.getId());

        assertThrows(ResourceException.class, () -> {
            userDbStorage.getUserById(user1.getId());
        });
    }

    @Test
    void isClearUsers() {
        List<User> expectedList = new ArrayList<>();

        userDbStorage.deleteAllUsers();
        List<User> actualListUsers = userDbStorage.getAllUsers();

        assertEquals(expectedList, actualListUsers, "Список не пуст");
    }

    @Test
    void isGetFilms() {
        List<Film> expected = new ArrayList<>();
        expected.add(film1);
        expected.add(film2);

        List<Film> actual = filmDbStorage.getAllFilms();

        assertEquals(expected, actual, "Списки фильмов не совпадают");
    }

    @Test
    public void isFindFilmById() {
        assertDoesNotThrow(() -> filmDbStorage.getFilmById(film1.getId()));
    }

    @Test
    void isUpdateFilm() throws ValidationException {
        Film filmUpd = Film.builder()
                .id(1)
                .name("film2")
                .description("descr2")
                .releaseDate(LocalDate.parse("2020-10-10"))
                .duration(120)
                .rate("1")
                .mpa(Mpa.builder().id(1).name("G").build())
                .genres(new TreeSet<>())
                .build();
        filmDbStorage.addFilm(filmUpd);

        Film actualFilm = filmDbStorage.updateFilm(filmUpd);

        assertEquals(filmUpd, actualFilm, "Фильмы не совпадают");
    }

    @Test
    void isAddLikeFilms() {
        filmDbStorage.addLike(film1.getId(), user1);
        filmDbStorage.addLike(film2.getId(), user1);
        filmDbStorage.addLike(film2.getId(), user2);
        List<Film> expected = new ArrayList<>();
        expected.add(film2);
        expected.add(film1);

        List<Film> popularFilms = filmDbStorage.getTopsFilms(2);

        assertEquals(expected, popularFilms, "Списки не совпадают");
    }


    @Test
    public void isDeleteFilmById() throws ValidationException {
        filmDbStorage.deleteFilmById(film1.getId());

        assertThrows(ResourceException.class, () -> {
            filmDbStorage.getFilmById(1);
        });
    }

    @Test
    void isClearFilms() {
        List<Film> expectedList = new ArrayList<>();

        filmDbStorage.deleteAllFilms();
        List<Film> actualListFilms = filmDbStorage.getAllFilms();

        assertEquals(expectedList, actualListFilms, "Список не пуст");
    }
}