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
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> get() {
        log.info("get films --OK");
        return filmStorage.findAll();
    }

    public Film get(int id) {
        return filmStorage.find(id).orElseThrow(() ->
                       new NotFoundException(HttpStatus.NOT_FOUND, "Bad id " + id + ". No film found"));
    }

    public Film add(Film film) {
        log.info("add film --OK");
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        if (!filmStorage.isAlreadyExist(film.getId())) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Bad id " + film.getId() + ". No film found");
        }
        filmStorage.update(film);
        log.info("update film --OK");
        return film;
    }

    public Film addLike(int filmId, int userId) {
        Film film = filmStorage.find(filmId).orElseThrow(() ->
                new NotFoundException(HttpStatus.NOT_FOUND, "Bad id " + filmId + ". No film found"));
        throwIfUserIdNotValid(userId);
        film.addLike(userId);
        log.info("add like --OK");
        return film;
    }

    public Film deleteLike(int filmId, int userId) {
        Film film = filmStorage.find(filmId).orElseThrow(() ->
                new NotFoundException(HttpStatus.NOT_FOUND, "Bad id " + filmId + ". No film found"));
        throwIfUserIdNotValid(userId);
        if (!film.getLikes().contains(userId)) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "No like for user id " + userId);
        }
        film.deleteLike(userId);
        log.info("delete like --OK");
        return film;
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

    private void throwIfUserIdNotValid(int userId) {
        if (!userStorage.findUsersId().contains(userId)) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Bad id " + userId + ". No user found");
        }
    }
}