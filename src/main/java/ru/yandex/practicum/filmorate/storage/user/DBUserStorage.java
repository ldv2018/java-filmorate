package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Qualifier
public class DBUserStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public DBUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> findAll() {
        String sqlQuery = "SELECT * FROM \"USER\"";
        return jdbcTemplate.query(sqlQuery, (resultSet, rowNum) -> makeUser(resultSet));
    }

    @Override
    public User add(User user) {
        String sqlQuery = "INSERT INTO \"USER\" (email, login, name, birthday) " +
                "VALUES (?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getName());
            statement.setDate(4, Date.valueOf(user.getBirthday()));
            return statement;
        }, keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return user;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE \"USER\" SET email = ?, login = ?, name = ?, birthday = ? "
                + "WHERE user_id = ?;";
        jdbcTemplate.update(
                sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    @Override
    public Optional<User> find(int id) {
        String sqlQuery = "SELECT * FROM \"USER\" WHERE user_id = " + id + ";";
        return jdbcTemplate.query(sqlQuery, (resultSet, rowNum) -> makeUser(resultSet))
                .stream()
                .findAny();
    }

    @Override
    public List<Integer> findId() {
        String sqlQuery = "SELECT user_id FROM \"USER\";";
        return jdbcTemplate.queryForList(sqlQuery, Integer.class);
    }

    @Override
    public boolean isAlreadyExist(int id) {
        return findId().contains(id);
    }

    @Override
    public void addFriend(int id, int friendId) {
        String sqlQuery = "INSERT INTO FRIEND (USER_ID, FRIEND_ID, FRIEND_STATUS) VALUES (?, ?, ?);";
        jdbcTemplate.update(sqlQuery,
                id,
                friendId,
                false);
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        String sqlQuery = "DELETE FROM FRIEND WHERE USER_ID = ? AND FRIEND_id = ?;";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public List<User> getFriends(int id) {
        String sqlQuery = "SELECT *" +
                "FROM \"USER\" " +
                "WHERE user_id IN (SELECT FRIEND_ID FROM FRIEND WHERE USER_ID = " + id +");";
        return jdbcTemplate.query(sqlQuery, (resultSet, rowNum) -> makeUser(resultSet));
    }

    @Override
    public List<User> getCommonFriends(int id, int otherId) {
        String sqlQuery = "SELECT * FROM \"USER\"" +
                "WHERE USER_ID IN (" +
                "SELECT FRIEND_ID FROM FRIEND WHERE USER_ID = " + id +
                " INTERSECT " +
                "SELECT FRIEND_ID FROM FRIEND WHERE USER_ID = " + otherId + ");";
        return jdbcTemplate.query(sqlQuery, (resultSet, rowNum) -> makeUser(resultSet));
    }

    private User makeUser(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("user_id");
        String email = resultSet.getString("email");
        String login = resultSet.getString("login");
        String name = resultSet.getString("name");
        LocalDate birthday = resultSet.getDate("birthday").toLocalDate();
        return new User(id, email, login, name, birthday);
    }
}
