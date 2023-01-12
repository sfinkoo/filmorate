package filmorate.storage;

import filmorate.models.User;

import java.util.List;

public interface UserStorage {

    User addUser(User user);

    User updateUser(User user);

    List<User> getAllUsers();

    User getUserById(int id);

    void addToFriends(int id, int idUser);

    void deleteFromFriends(int id, int idUser);

    void deleteUserById(int id);

    void deleteAllUsers();

    List<User> getGeneralListFriends(int idUser1, int idUser2);

    List<User> getFriendsById(int id);
}

