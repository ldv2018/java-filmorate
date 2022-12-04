package ru.yandex.practicum.filmorate.service.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private static final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getFilms() {
        return filmStorage.findAll();
    }

    public Film getFilmById(int id) {
        return Optional.ofNullable(filmStorage.findFilmById(id))
                .orElseThrow(() ->
                       new NotFoundException(HttpStatus.NOT_FOUND, "Bad id " + id + ". No film found"));
    }

    public Film addFilm(Film film) {
        if (film == null) {
            throw new BadRequestException(HttpStatus.NOT_FOUND, "Bad request. Film couldn't be null");
        }
        log.info("add film --OK");
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        if (film == null) {
            throw new BadRequestException(HttpStatus.NOT_FOUND, "Bad request. Film couldn't be null");
        }
        if (!filmStorage.findFilmsId().contains(film.getId())) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Bad id " + film.getId() + ". No film found");
        }
        filmStorage.updateFilm(film);
        log.info("update film --OK");
        return film;
    }

    public Film addLike(int filmId, int userId) {
        checkFilmAndUserId(filmId, userId);
        filmStorage.findFilmById(filmId).addLike(userId);
        log.info("add like --OK");
        return filmStorage.findFilmById(filmId);
    }

    public Film deleteLike(int filmId, int userId) {
        checkFilmAndUserId(filmId, userId);
        if (!filmStorage.findFilmById(filmId).getLikes().contains(userId)) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "No like for user id " + userId);
        }
        return filmStorage.deleteLike(filmId, userId);
    }

    public List<Film> getPopularFilms(Integer count) {
        if (count > filmStorage.findAll().size()) {
            count = filmStorage.findAll().size();
        }

        return filmStorage.findAll()
                .stream()
                .sorted((f1, f2) -> Math.toIntExact(f2.getPopularityIndex() - f1.getPopularityIndex()))
                .limit(count)
                .collect(Collectors.toList());
    }

    private void checkFilmAndUserId(int filmId, int userId) {
        if (!filmStorage.findFilmsId().contains(filmId)) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Bad id " + filmId + ". No film found");
        }
        if (!userStorage.findUsersId().contains(userId)) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Bad id " + userId + ". No user found");
        }
        if (filmId <= 0) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Film id must be positive");
        }
        if (userId <= 0) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "User id must be positive");
        }
    }
}