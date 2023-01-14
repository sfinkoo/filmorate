package filmorate.service;

import filmorate.models.User;
import filmorate.storage.UserStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDao") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        userStorage.addUser(user);
        log.debug("Пользователь успешно добавлен.");
        return user;
    }

    public User updateUser(User user) {
        userStorage.updateUser(user);
        log.debug("Информация о пользователе успешно обновлена.");
        return user;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public void deleteFriend(int idUser, int idFriend) {
        userStorage.deleteFromFriends(idUser, idFriend);
        log.debug("Удален из списка друзей.");
    }

    public List<User> getFriendsById(int id) {
        return userStorage.getFriendsById(id);
    }

    public List<User> getGeneralListFriends(int id, int otherId) {
        return userStorage.getGeneralListFriends(id, otherId);
    }

    public void deleteUserById(int id) {
        userStorage.deleteUserById(id);
    }

    public void deleteAllUsers() {
        userStorage.deleteAllUsers();
    }

    public void addToFriends(int idUser, int idFriend) {
        userStorage.addToFriends(idUser, idFriend);
    }
}
