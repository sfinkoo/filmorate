package filmorate.dao.impl;

import filmorate.exception.ResourceException;
import filmorate.models.User;
import filmorate.storage.UserStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component("userDao")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        String sql =
                "INSERT INTO USERS (NAME, LOGIN, EMAIL, BIRTHDAY) VALUES ((?), (?), (?), (?))";
        jdbcTemplate.update(sql,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday());

        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from USERS where LOGIN=?", user.getLogin());
        if (userRows.next()) {
            user.setId(userRows.getInt("id"));
        }
        return user;
    }

    @Override
    public User updateUser(User user) {
        checkId(user.getId());
        String sql =
                "UPDATE USERS SET NAME = ?, LOGIN = ?, EMAIL = ?, BIRTHDAY = ? " + "WHERE ID = ?";
        jdbcTemplate.update(sql,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "select id, name, login, email, birthday from users";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User getUserById(int id) {
        try {
            String sqlQuery = "select * from USERS where ID= " + id;
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser);
        } catch (DataAccessException dataAccessException) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Пользователь с таким id не найден.");
        }
    }

    @Override
    public void addToFriends(int idUser, int idFriend) {
        checkId(idFriend);
        checkId(idUser);
        String sql = "INSERT INTO FRIENDSHIP (USER_ID, FRIEND_ID) VALUES (?, ?)";
        jdbcTemplate.update(sql, idUser, idFriend);
        log.info("Пользователь {} добавлен в подписки к пользователю {}", idFriend, idUser);
    }

    @Override
    public void deleteFromFriends(int idFriend, int idUser) {
        String sql = "DELETE FROM FRIENDSHIP WHERE USER_ID=? AND FRIEND_ID=?";
        jdbcTemplate.update(sql, idFriend, idUser);
        log.info("Пользователь {} удален из подписок пользователя {}", idFriend, idUser);
    }

    @Override
    public void deleteUserById(int id) {
        checkId(id);
        String sqlDelFr = "DELETE FROM FRIENDSHIP WHERE USER_ID=? OR FRIEND_ID=?";
        jdbcTemplate.update(sqlDelFr, id, id);
        String sqlDropLike = "DELETE FROM LIKES WHERE USER_ID=?";
        jdbcTemplate.update(sqlDropLike, id);
        String sqlDelUs = "DELETE from USERS where ID=?";
        jdbcTemplate.update(sqlDelUs, id);
    }

    @Override
    public void deleteAllUsers() {
        String sqlDropFr = "DELETE FROM FRIENDSHIP";
        jdbcTemplate.update(sqlDropFr);
        String sqlDropLike = "DELETE FROM LIKES";
        jdbcTemplate.update(sqlDropLike);
        String sqlDelUsers = "DELETE from USERS";
        jdbcTemplate.update(sqlDelUsers);
        log.info("Удалены все пользователи таблицы USERS");
    }

    @Override
    public List<User> getFriendsById(int id) {
        try {
            String sql = "SELECT USERS.ID, USERS.NAME, USERS.LOGIN, USERS.EMAIL, USERS.BIRTHDAY" +
                    " FROM USERS" +
                    " WHERE USERS.ID in (" +
                    "           SELECT FRIEND_ID FROM FRIENDSHIP WHERE USER_ID = ?)";

            return jdbcTemplate.query(sql, this::mapRowToUser, id);
        } catch (Exception e) {
            log.info("У пользователя с id {} в подписках нет друзей", id);
            return new ArrayList<>();
        }
    }

    @Override
    public List<User> getGeneralListFriends(int idUser1, int idUser2) {
        try {
            String sql = "SELECT U.ID, U.EMAIL, U.NAME, U.LOGIN, U.BIRTHDAY " +
                    "FROM FRIENDSHIP FR " +
                    "LEFT JOIN USERS U on U.ID = FR.FRIEND_ID " +
                    "WHERE USER_ID = ? " +
                    "   OR USER_ID = ? " +
                    "GROUP BY U.ID, U.NAME, U.NAME, U.LOGIN, U.BIRTHDAY " +
                    "HAVING COUNT(FRIEND_ID) > 1";
            return jdbcTemplate.query(sql, this::mapRowToUser, idUser1, idUser2);
        } catch (Exception e) {
            log.info("У пользователей с id {} и {} в нет общих друзей", idUser1, idUser2);
            return new ArrayList<>();
        }
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .login(resultSet.getString("login"))
                .email(resultSet.getString("email"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }

    private void checkId(int id) {
        if (getUserById(id) == null) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Пользователь с id = " + id + " не найден.");
        } else if (id < 0) {
            throw new ResourceException(HttpStatus.BAD_REQUEST, "Отрицательные значения не допустимы.");
        }
    }
}
