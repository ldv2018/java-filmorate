package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class DbMpaStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public DbMpaStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> findAll() {
        String sqlQuery = "SELECT * FROM RATING;";

        return jdbcTemplate.query(sqlQuery, (ResultSet, rowNum) -> makeMPA(ResultSet));
    }

    @Override
    public Optional<Mpa> find(int id) {
        String sqlQuery = "SELECT * FROM RATING WHERE RATING_ID = " + id + ";";

        return jdbcTemplate.query(sqlQuery, (ResultSet, rowNum) -> makeMPA(ResultSet))
                .stream()
                .findAny();
    }

    private Mpa makeMPA(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("rating_id");
        String name = resultSet.getString("name");

        return new Mpa(id, name);
    }
}
