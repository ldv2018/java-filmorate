package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.exceptions.ConflictException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.*;

@RestController
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @GetMapping("/films")
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping(path = "/films")
    public Film addFilm(@RequestBody Film film) {
        if (film == null) {
            throw new BadRequestException(HttpStatus.NOT_FOUND, "Bad request. Film couldn't be null");
        }
        FilmValidator.validateFilm(film);
        film.setId(id);
        films.put(id, film);
        log.info("film added");
        id++;
        return film;
    }

    @PutMapping(path = "/films")
    public Film updateFilm(@RequestBody Film film) {
        if (film == null) {
            throw new BadRequestException(HttpStatus.NOT_FOUND, "Bad request. Film couldn't be null");
        }
        if (!films.containsKey(film.getId())) {
            throw new ConflictException(HttpStatus.NOT_FOUND, "Bad id. No film found");
        }
        FilmValidator.validateFilm(film);
        films.replace(film.getId(), film);
        log.info("film updated");
        return film;
    }
}