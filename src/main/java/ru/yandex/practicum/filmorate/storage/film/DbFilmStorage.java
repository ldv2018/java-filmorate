package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Qualifier
@Component
public class DbFilmStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public DbFilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> findAll() {
        String sqlQuery = "SELECT FILM_ID, " +
                "FILM.NAME, " +
                "DESCRIPTION, " +
                "RELEASE_DATE, " +
                "DURATION, " +
                "FILM.RATING_ID, " +
                "RATING.NAME AS RATING_NAME " +
                "FROM FILM " +
                "JOIN RATING on FILM.RATING_ID = RATING.RATING_ID";

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
            statement.setInt(5, film.getMpa().getId());
            return statement;
        }, keyHolder);

        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        if (film.getGenres() != null) {
            List<Genre> genres = film.getGenres();
            for (Genre g : genres) {
                jdbcTemplate.update(
                        "INSERT INTO FILM_TO_GENRE VALUES (?, ?)",
                        film.getId(), g.getId()
                );
            }
        }

        return film;
    }

    @Override
    public Film update(Film film) {
        String sQuery = "DELETE FROM FILM_TO_GENRE WHERE FILM_ID = ?;";
        jdbcTemplate.update(sQuery, film.getId());

        String sqlQuery = "UPDATE FILM SET " +
                "NAME = ?, " +
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
                film.getMpa().getId()
        );
        if (film.getGenres() != null) {
            if (!film.getGenres().isEmpty()) {
                List<Genre> genres = film.getGenres()
                        .stream()
                        .distinct().collect(Collectors.toList());
                for (Genre g : genres) {
                        jdbcTemplate.update(
                                "INSERT INTO FILM_TO_GENRE VALUES (?, ?)",
                                film.getId(), g.getId()
                        );
                }
            }
        }

        return find(film.getId()).orElseThrow();
    }

    @Override
    public Optional<Film> find(int id) {
        String sqlQuery = "SELECT FILM_ID, " +
                "FILM.NAME, " +
                "DESCRIPTION, " +
                "RELEASE_DATE, " +
                "DURATION, " +
                "FILM.RATING_ID, " +
                "RATING.NAME AS RATING_NAME " +
                "FROM FILM " +
                "JOIN RATING ON RATING.RATING_ID = FILM.RATING_ID WHERE FILM_ID = " + id + ";";

        return jdbcTemplate.query(sqlQuery, (resultSet, rowNum) -> makeFilm(resultSet))
                .stream()
                .findAny();
    }

    @Override
    public boolean isAlreadyExist(int id) {
        String sqlQuery = "SELECT FILM_ID FROM FILM;";

        return jdbcTemplate.queryForList(sqlQuery, Integer.class).contains(id);
    }

    @Override
    public Film addLike(int filmId, int userId) {
        String sqlQuery = "INSERT INTO FILM_LIKE SET FILM_ID = ?, USER_ID = ?";
        jdbcTemplate.update(
                sqlQuery,
                filmId,
                userId
        );

        return find(filmId).orElseThrow(() ->
                new NotFoundException(HttpStatus.NOT_FOUND, "Bad id " + filmId + ". No film found"));
    }

    @Override
    public Film deleteLike(int filmId, int userId) {
        String sqlQuery = "DELETE FROM FILM_LIKE WHERE FILM_ID = ? AND USER_ID = ?;";
        jdbcTemplate.update(sqlQuery, filmId, userId);

        return find(filmId).orElseThrow(() ->
                new NotFoundException(HttpStatus.NOT_FOUND, "Bad id " + filmId + ". No film found"));
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        String sqlQuery = "SELECT FILM.FILM_ID, " +
                "FILM.NAME, " +
                "DESCRIPTION, " +
                "RELEASE_DATE, " +
                "DURATION, " +
                "FILM.RATING_ID, " +
                "RATING.NAME AS RATING_NAME " +
                "FROM FILM " +
                "JOIN RATING on FILM.RATING_ID = RATING.RATING_ID " +
                "LEFT JOIN (SELECT FILM_ID, " +
                "COUNT(USER_ID) rate " +
                "FROM FILM_LIKE " +
                "GROUP BY FILM_ID " +
                ") r ON FILM.FILM_ID = r.FILM_ID " +
                "ORDER BY r.rate DESC " +
                "LIMIT " + count + ";";
        
        return jdbcTemplate.query(sqlQuery, (resultSet, rowNum) -> makeFilm(resultSet));
    }

    private Film makeFilm(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("film_id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        LocalDate releaseDate = resultSet.getDate("release_date").toLocalDate();
        int duration = resultSet.getInt("duration");
        Mpa mpa = new Mpa(resultSet.getInt("rating_id"),
                resultSet.getString("rating_name"));

        return new Film(id,
                name,
                description,
                releaseDate,
                duration,
                mpa);
    }
}
