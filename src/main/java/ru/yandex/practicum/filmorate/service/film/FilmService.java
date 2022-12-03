package ru.yandex.practicum.filmorate.service.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.exceptions.ConflictException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private static final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private int id = 1;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(int id) {
        if (!filmStorage.getFilmsId().contains(id)) {
            throw new ConflictException(HttpStatus.NOT_FOUND, "Bad id " + id + ". No film found");
        }
        return filmStorage.getFilmById(id);
    }

    public Film addFilm(Film film) {
        if (film == null) {
            throw new BadRequestException(HttpStatus.NOT_FOUND, "Bad request. Film couldn't be null");
        }
        film.setId(id);
        id++;
        log.info("add film --OK");
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        if (film == null) {
            throw new BadRequestException(HttpStatus.NOT_FOUND, "Bad request. Film couldn't be null");
        }
        if (!filmStorage.getFilmsId().contains(film.getId())) {
            throw new ConflictException(HttpStatus.NOT_FOUND, "Bad id " + film.getId() + ". No film found");
        }
        filmStorage.updateFilm(film);
        log.info("update film --OK");
        return film;
    }

    public Film addLike(int filmId, int userId) {
        checkFilmAndUserId(filmId, userId);
        filmStorage.getFilmById(filmId).addLike(userId);
        log.info("add like --OK");
        return filmStorage.getFilmById(filmId);
    }

    public Film deleteLike(int filmId, int userId) {
        checkFilmAndUserId(filmId, userId);
        if (!filmStorage.getFilmById(filmId).getLikes().contains(userId)) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "No like for user id " + userId);
        }
        return filmStorage.deleteLike(filmId, userId);
    }

    public List<Film> getPopularFilms(Integer count) {
        if (count > filmStorage.getFilms().size()) {
            count = filmStorage.getFilms().size();
        }

        return filmStorage.getFilms()
                .stream()
                .sorted((f1, f2) -> Math.toIntExact(f2.getPopularityIndex() - f1.getPopularityIndex()))
                .limit(count)
                .collect(Collectors.toList());
    }

    private void checkFilmAndUserId(int filmId, int userId) {
        if (!filmStorage.getFilmsId().contains(filmId)) {
            throw new ConflictException(HttpStatus.NOT_FOUND, "Bad id " + filmId + ". No film found");
        }
        if (!userStorage.getUsersId().contains(userId)) {
            throw new ConflictException(HttpStatus.NOT_FOUND, "Bad id " + userId + ". No user found");
        }
        if (filmId <= 0) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Film id must be positive");
        }
        if (userId <= 0) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "User id must be positive");
        }
    }
}