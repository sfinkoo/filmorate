package filmorate.service;

import filmorate.exception.ResourceException;
import filmorate.exception.ValidationException;
import filmorate.models.User;
import filmorate.storage.UserStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    private final static Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) throws ValidationException {
        validateBirthday(user);
        userStorage.addUser(user);
        log.debug("Пользователь успешно добавлен.");
        return user;
    }

    public User updateUser(User user) {
        validateContainsId(user.getId());
        userStorage.updateUser(user);
        log.debug("Информация о пользователе успешно обновлена.");
        return user;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(int id) {
        validateContainsId(id);
        return userStorage.getUserByID(id);
    }

    public void addFriend(int id, int friendId) {
        validateContainsId(friendId);
        userStorage.addToFriends(id, friendId);
        log.debug("Теперь вы в списке друзей.");
    }

    public void deleteFriend(int id, int friendId) {
        userStorage.deleteFromFriends(id, friendId);
        log.debug("Удален из списка друзей.");
    }

    public List<User> getFriendsById(int id) {
        List<Integer> idFriends = new ArrayList<>(userStorage.getUserByID(id).getFriends());
        List<User> friends = new ArrayList<>();
        for (int idF : idFriends) {
            friends.add(userStorage.getUserByID(idF));
        }
        return friends;
    }

    public List<User> getGeneralListFriends(int id, int otherId) {
        Set<Integer> friends1 = userStorage.getUserByID(id).getFriends();
        Set<Integer> friends2 = userStorage.getUserByID(otherId).getFriends();
        List<Integer> commonIdFriends = friends1.stream()
                .filter(friends2::contains)
                .collect(Collectors.toList());
        List<User> commonFriends = new ArrayList<>();
        for (int idF : commonIdFriends) {
            commonFriends.add(userStorage.getUserByID(idF));
        }
        if (commonFriends.isEmpty()) {
            log.debug("Общих друзей нет.");
        }
        return commonFriends;
    }


    private void validateBirthday(User user) throws ValidationException {
        if (user.getBirthday().isAfter(ChronoLocalDate.from(LocalDate.now()))) {
            throw new ValidationException("Проверьте данные и сделайте повторный запрос.");
        }
    }

    private void validateContainsId(int id) {
        if (userStorage.getUserByID(id) == null) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Пользователь с таким id не найден.");
        }
    }
}
