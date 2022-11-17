package filmorate.storage;

import filmorate.models.User;

import java.util.List;

public interface UserStorage {

    void addUser(User user);

    void updateUser(User user);

    List<User> getAllUsers();

    List<Integer> getAllIDs();

    User getUserByID(int id);

    void addToFriends(int id, int idUser);

    void deleteFromFriends(int id, int idUser);

}

