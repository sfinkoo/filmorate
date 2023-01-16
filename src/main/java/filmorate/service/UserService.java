package filmorate.service;

import filmorate.models.User;
import filmorate.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userDao;

    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        userDao.addUser(user);
        log.debug("Пользователь успешно добавлен.");
        return user;
    }

    public User updateUser(User user) {
        userDao.updateUser(user);
        log.debug("Информация о пользователе успешно обновлена.");
        return user;
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public User getUserById(int id) {
        return userDao.getUserById(id);
    }

    public void deleteFriend(int idUser, int idFriend) {
        userDao.deleteFromFriends(idUser, idFriend);
        log.debug("Удален из списка друзей.");
    }

    public List<User> getFriendsById(int id) {
        return userDao.getFriendsById(id);
    }

    public List<User> getGeneralListFriends(int id, int otherId) {
        return userDao.getGeneralListFriends(id, otherId);
    }

    public void deleteUserById(int id) {
        userDao.deleteUserById(id);
    }

    public void deleteAllUsers() {
        userDao.deleteAllUsers();
    }

    public void addToFriends(int idUser, int idFriend) {
        userDao.addToFriends(idUser, idFriend);
    }
}
