package ru.yandex.practicum.filmorate.storage.genre;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class DBGenreStorage implements GenreStorage{
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> findAll() {
        String sqlQuery = "SELECT * FROM GENRE;";
        return jdbcTemplate.query(sqlQuery, (resultSet, rowNum) -> makeGenre(resultSet));
    }

    @Override
    public Optional<Genre> find(int id) {
        String sqlQuery = "SELECT * FROM GENRE WHERE GENRE_ID = " + id + ";";
        return jdbcTemplate.query(sqlQuery, (resultSet, rowNum) -> makeGenre(resultSet))
                .stream()
                .findAny();
    }

    @Override
    public List<Genre> findByFilm(int filmId) {
        String sqlQuery = "SELECT FILM_TO_GENRE.GENRE_ID, " +
                "GENRE.NAME " +
                "FROM FILM_TO_GENRE " +
                "JOIN GENRE ON FILM_TO_GENRE.GENRE_ID = GENRE.GENRE_ID " +
                "WHERE FILM_ID = " + filmId + ";";
        return jdbcTemplate.query(sqlQuery, (resultSet, rowNum) -> makeGenre(resultSet));
    }

    private Genre makeGenre(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("genre_id");
        String name = resultSet.getString("name");
        return new Genre(id, name);
    }

}
