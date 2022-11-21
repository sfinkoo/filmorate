package filmorate.service;

import filmorate.exception.ResourceException;
import filmorate.models.User;
import filmorate.storage.UserStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
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
        return userStorage.getUserByID(id).getFriends().stream()
                .map(userStorage::getUserByID)
                .collect(Collectors.toList());
    }

    public List<User> getGeneralListFriends(int id, int otherId) {
        Set<Integer> friends1 = userStorage.getUserByID(id).getFriends();
        Set<Integer> friends2 = userStorage.getUserByID(otherId).getFriends();
        List<Integer> commonIdFriends = friends1.stream()
                .filter(friends2::contains)
                .collect(Collectors.toList());
        List<User> commonFriends = new ArrayList<>();
        for (int idFriends : commonIdFriends) {
            commonFriends.add(userStorage.getUserByID(idFriends));
        }
        if (commonFriends.isEmpty()) {
            log.debug("Общих друзей нет.");
        }
        return commonFriends;
    }

    private void validateContainsId(int id) {
        if (userStorage.getUserByID(id) == null) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Пользователь с таким id не найден.");
        }
    }
}
