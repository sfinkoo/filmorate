package filmorate.controller;

import filmorate.IdCreator;
import filmorate.exception.ResourceException;
import filmorate.exception.ValidationException;
import filmorate.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final IdCreator idCreator = new IdCreator();
    private final Map<Integer, User> users = new HashMap<>();
    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping
    public User addUser(@Valid @RequestBody User user) throws ValidationException {
        log.debug("Количество пользователей до добавления: {}", users.size());
        if (validateName(user)) {
            log.debug("Переданы некорректные данные.");
            throw new ValidationException("Проверьте данные и сделайте повторный запрос.");
        }
        user.setId(idCreator.createId());
        users.put(user.getId(), user);
        log.debug("Количество пользователей после добавления: {}", users.size());
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        if (!validateContainsId(user)) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Пользователь с таким id не найден.");
        }
        users.put(user.getId(), user);
        log.debug("Информация о пользователе успешно обновлена.");
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    private boolean validateName(User user) {
        return user.getBirthday().isAfter(ChronoLocalDate.from(LocalDate.now()));
    }

    private boolean validateContainsId(User user) {
        return users.containsKey(user.getId());
    }
}
