package ru.yandex.practicum.filmorate.service.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class FilmService {

    private final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(@Qualifier("DBFilmStorage")FilmStorage filmStorage, @Qualifier("DBUserStorage") UserStorage userStorage) {
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
        if(!userStorage.isAlreadyExist(userId)) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Bad id " + filmId + ". No user found");
        }
        if(!filmStorage.isAlreadyExist(filmId)) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Bad id " + filmId + ". No film found");
        }
        Film film = filmStorage.addLike(filmId, userId).orElseThrow();
        log.info("add like --OK");
        return film;
    }

    public Film deleteLike(int filmId, int userId) {
        if(!userStorage.isAlreadyExist(userId)) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Bad id " + filmId + ". No user found");
        }
        if(!filmStorage.isAlreadyExist(filmId)) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Bad id " + filmId + ". No film found");
        }
        Film film = filmStorage.deleteLike(filmId, userId).orElseThrow();
        log.info("delete like --OK");
        return film;
    }

    public List<Film> getPopularFilms(Integer count) {
        if (count > filmStorage.findAll().size()) {
            count = filmStorage.findAll().size();
        }
        return filmStorage.getPopularFilms(count);
    }
}