package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class DBMPAStorage implements MPAStorage{
    private final JdbcTemplate jdbcTemplate;

    public DBMPAStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MPA> findAll() {
        String sqlQuery = "SELECT * FROM RATING;";
        return jdbcTemplate.query(sqlQuery, (ResultSet, rowNum) -> makeMPA(ResultSet));
    }

    @Override
    public Optional<MPA> find(int id) {
        String sqlQuery = "SELECT * FROM RATING WHERE RATING_ID = " + id + ";";
        return jdbcTemplate.query(sqlQuery, (ResultSet, rowNum) -> makeMPA(ResultSet))
                .stream()
                .findAny();
    }

    private MPA makeMPA(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("rating_id");
        String name = resultSet.getString("name");
        return new MPA(id, name);
    }
}
