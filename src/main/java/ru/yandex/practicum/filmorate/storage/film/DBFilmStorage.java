package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.genre.GenreService;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Qualifier
@Component
public class DBFilmStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreService genreService;

    public DBFilmStorage(JdbcTemplate jdbcTemplate, GenreService genreService) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreService = genreService;
    }

    @Override
    public List<Film> findAll() {
        String sqlQuery = "SELECT * FROM FILM;";
        return jdbcTemplate.query(sqlQuery, (resultSet, rowNum) -> makeFilm(resultSet));
    }

    @Override
    public Film add(Film film) {
        String sqlQuery = "INSERT INTO FILM (NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID) " +
                "VALUES (?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            statement.setString(1, film.getName());
            statement.setString(2, film.getDescription());
            statement.setDate(3, Date.valueOf(film.getReleaseDate()));
            statement.setInt(4, film.getDuration());
            statement.setInt(5, film.getMPA());
            return statement;
        }, keyHolder);

        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return film;
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE FILM SET NAME = ?, " +
                "DESCRIPTION = ?, " +
                "RELEASE_DATE = ?, " +
                "DURATION = ?, " +
                "RATING_ID = ? " +
                "WHERE FILM_ID = " + film.getId() + ";";
        jdbcTemplate.update(
                sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMPA()
        );
        return film;
    }

    @Override
    public Optional<Film> find(int id) {
        String sqlQuery = "SELECT * FROM FILM WHERE FILM_ID = " + id + ";";
        return jdbcTemplate.query(sqlQuery, (resultSet, rowNum) -> makeFilm(resultSet))
                .stream()
                .findAny();
    }

    @Override
    public List<Integer> findId() {
        String sqlQuery = "SELECT FILM_ID FROM FILM;";
        return jdbcTemplate.queryForList(sqlQuery, Integer.class);
    }

    @Override
    public boolean isAlreadyExist(int id) {
        return findId().contains(id);
    }

    @Override
    public Optional<Film> addLike(int filmId, int userId) {
        String sqlQuery = "INSERT INTO FILM_LIKE SET FILM_ID = ?, USER_ID = ?";
        jdbcTemplate.update(
                sqlQuery,
                filmId,
                userId
        );
        return find(filmId);
    }

    @Override
    public Optional<Film> deleteLike(int filmId, int userId) {
        String sqlQuery = "DELETE FROM FILM_LIKE WHERE FILM_ID = ? AND USER_ID = ?;";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        return find(filmId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        String sqlQuery = "SELECT * FROM FILM WHERE FILM_ID IN (" +
                "SELECT FILM_ID, COUNT(USER_ID) FROM FILM_LIKE GROUP BY FILM_ID " +
                "ORDER BY COUNT(USER_ID) DESC" +
                ") LIMIT " + count + ";";
        return jdbcTemplate.query(sqlQuery, (resultSet, rowNum) -> makeFilm(resultSet));

    }

    private Film makeFilm(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("film_id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        LocalDate releaseDate = resultSet.getDate("release_date").toLocalDate();
        int duration = resultSet.getInt("duration");
        List<Integer> genres = genreService.getByFilm(id);
        int MPA = resultSet.getInt("rating_id");
        return new Film(id,
                name,
                description,
                releaseDate,
                duration,
                genres,
                MPA);
    }
}
