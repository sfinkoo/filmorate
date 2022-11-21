package filmorate.storage;

import filmorate.models.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final IdCreator idCreator = new IdCreator();
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public void addUser(User user) {
        user.setId(idCreator.createId());
        users.put(user.getId(), user);
    }

    @Override
    public void updateUser(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserByID(int id) {
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
}