package ru.yandex.practicum.filmorate.storage.genre;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@AllArgsConstructor
public class DbGenreStorage implements GenreStorage{
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
    public List<Genre> findByFilmId(int filmId) {
        String sqlQuery = "SELECT FILM_TO_GENRE.GENRE_ID, " +
                "GENRE.NAME " +
                "FROM FILM_TO_GENRE " +
                "JOIN GENRE ON FILM_TO_GENRE.GENRE_ID = GENRE.GENRE_ID " +
                "WHERE FILM_ID = " + filmId + ";";

        return jdbcTemplate.query(sqlQuery, (resultSet, rowNum) -> makeGenre(resultSet));
    }

    @Override
    public Map<Integer, List<Genre>> findAllToFilm() {
        String sqlQuery = "SELECT FILM_TO_GENRE.FILM_ID, " +
                "FILM_TO_GENRE.GENRE_ID, " +
                "GENRE.NAME " +
                "FROM FILM_TO_GENRE " +
                "JOIN GENRE ON FILM_TO_GENRE.GENRE_ID = GENRE.GENRE_ID;";

        return jdbcTemplate.query(sqlQuery, this::makeGenreToFilm);
    }

    private Map<Integer, List<Genre>> makeGenreToFilm(ResultSet resultSet) throws SQLException {
        Map<Integer, List<Genre>> genreToFilm = new LinkedHashMap<>();
        while(resultSet.next()) {
            Integer filmId = resultSet.getInt("film_id");
            genreToFilm.putIfAbsent(filmId, new ArrayList<>());
            int genreId = resultSet.getInt("genre_id");
            String name = resultSet.getString("name");
            genreToFilm.get(filmId).add(new Genre(genreId, name));
        }

        return genreToFilm;
    }

    private Genre makeGenre(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("genre_id");
        String name = resultSet.getString("name");

        return new Genre(id, name);
    }
}