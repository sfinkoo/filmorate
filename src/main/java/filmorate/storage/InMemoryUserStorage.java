package filmorate.storage;

import filmorate.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User addUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(int id) {
        return users.get(id);
    }

    @Override
    public void addToFriends(int id, int idFriend) {
        users.get(id).getFriends().add(idFriend);
        users.get(idFriend).getFriends().add(id);
    }

    @Override
    public void deleteFromFriends(int id, int idFriend) {
        users.get(id).getFriends().remove(idFriend);
        users.get(idFriend).getFriends().remove(idFriend);
    }

    @Override
    public List<User> getFriendsById(int id) {
        return getUserById(id).getFriends().stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUserById(int id) {
    }

    @Override
    public void deleteAllUsers() {
    }

    @Override
    public List<User> getGeneralListFriends(int idUser1, int idUser2) {
        Set<Integer> friends1 = getUserById(idUser1).getFriends();
        Set<Integer> friends2 = getUserById(idUser2).getFriends();
        List<Integer> commonIdFriends = friends1.stream()
                .filter(friends2::contains)
                .collect(Collectors.toList());
        List<User> commonFriends = new ArrayList<>();
        for (int idFriends : commonIdFriends) {
            commonFriends.add(getUserById(idFriends));
        }
        if (commonFriends.isEmpty()) {
            log.debug("Общих друзей нет.");
        }
        return commonFriends;
    }
}