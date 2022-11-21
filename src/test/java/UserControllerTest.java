import filmorate.controller.UserController;
import filmorate.models.User;
import filmorate.service.UserService;
import filmorate.storage.IdCreator;
import filmorate.storage.InMemoryFilmStorage;
import filmorate.storage.InMemoryUserStorage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = {
        UserController.class,
        InMemoryFilmStorage.class,
        UserService.class,
        InMemoryUserStorage.class
})
public class UserControllerTest {

    @Autowired
    private UserController userController;

    private final IdCreator idCreator = new IdCreator();
    private final User user = new User(idCreator.createId(), "fiiinko@mail.ru", "finko",
            "Sofya", "2000-09-21");

    @Test
    public void addUserTest() {
        userController.addUser(user);
        assertTrue(userController.getAllUsers().contains(user));
    }

    @Test
    public void updateUserTest() {
        User addUser = userController.addUser(user);
        User userForUpdate = new User(addUser.getId(), "fiiinko@mail.ru", "finko",
                "Sonya", "2000-09-21");
        userController.updateUser(userForUpdate);
        assertEquals(userController.getAllUsers().get(addUser.getId() - 1), userForUpdate);
    }

    @Test
    public void getAllUsersTest() {
        assertNotNull(userController.getAllUsers());
    }
}
