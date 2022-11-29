package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage{

    private static final Logger log = LoggerFactory.getLogger(InMemoryFilmStorage.class);
    private Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(id);
        films.put(id, film);
        id++;
        log.info("film added");
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new RuntimeException(); //поменяй потом исключение
        }
        films.replace(film.getId(), film);
        log.info("film updated");
        return film;
    }
}
