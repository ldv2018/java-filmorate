package ru.yandex.practicum.filmorate.service.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Map;

@Service
public class FilmService {

    private final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreStorage genreStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage,
                       UserStorage userStorage,
                       GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
    }

    public List<Film> get() {
        log.info("start FilmService.get()");
        List<Film> films = filmStorage.findAll();
        Map<Integer, List<Genre>> filmsGenres = genreStorage.findAllToFilm();
        for (Film film : films) {
            if (filmsGenres.containsKey(film.getId())) {
                for (Genre genre : filmsGenres.get(film.getId())) {
                    film.addGenres(genre);
                }
            }
        }
        log.info("get films --OK");

        return films;
    }

    public Film get(int id) {
        log.info("start FilmService.get(id)");
        log.info("get film --OK");
        Film film = filmStorage.find(id).orElseThrow(() ->
                new NotFoundException(HttpStatus.NOT_FOUND, "Bad id " + id + ". No film found"));
        List<Genre> genres = genreStorage.findByFilmId(id);
        for (Genre genre : genres) {
            film.addGenres(genre);
        }

        return film;
    }

    public Film add(Film film) {
        log.info("start FilmService.add(film)");
        log.info("add film --OK");

        return filmStorage.add(film);
    }

    public Film update(Film film) {
        log.info("start FilmService.update(film)");
        if (!filmStorage.isAlreadyExist(film.getId())) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Bad id " + film.getId() + ". No film found");
        }
        log.info("update film --OK");
        Film updatedFilm = filmStorage.update(film);
        List<Genre> genres = genreStorage.findByFilmId(film.getId());
        for (Genre genre : genres) {
            updatedFilm.addGenres(genre);
        }

        return updatedFilm;
    }

    public Film addLike(int filmId, int userId) {
        log.info("start FilmService.addLike(id, id)");
        if (!userStorage.isAlreadyExist(userId)) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Bad id " + filmId + ". No user found");
        }
        if (!filmStorage.isAlreadyExist(filmId)) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Bad id " + filmId + ". No film found");
        }
        Film film = filmStorage.addLike(filmId, userId);
        log.info("add like --OK");

        return film;
    }

    public Film deleteLike(int filmId, int userId) {
        log.info("start FilmService.deleteLike(id, id)");
        if (!userStorage.isAlreadyExist(userId)) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Bad id " + filmId + ". No user found");
        }
        if (!filmStorage.isAlreadyExist(filmId)) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Bad id " + filmId + ". No film found");
        }
        Film film = filmStorage.deleteLike(filmId, userId);
        log.info("delete like --OK");

        return film;
    }

    public List<Film> getPopularFilms(Integer count) {
        log.info("start FilmService.getPopularFilms(count)");
        if (count > filmStorage.findAll().size()) {
            count = filmStorage.findAll().size();
        }

        return filmStorage.getPopularFilms(count);
    }
}