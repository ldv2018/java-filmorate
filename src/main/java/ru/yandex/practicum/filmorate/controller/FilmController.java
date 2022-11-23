package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.SaveExeption;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.*;

@RestController
public class FilmController {

    private final HashMap<Integer, Film> films = new HashMap<>();
    private int id = 1;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping("/films")
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping(path = "/films")
    public Film addFilm(@RequestBody Film film) {
        if (film == null) {
            throw new SaveExeption("film save failed");
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
        if (film == null || !films.containsKey(film.getId())) {
            throw new SaveExeption("film update failed");
        }
        FilmValidator.validateFilm(film);
        films.replace(film.getId(), film);
        log.info("film updated");
        return film;
    }







}
