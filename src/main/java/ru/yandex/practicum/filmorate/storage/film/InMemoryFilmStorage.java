package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage{

    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film findFilmById(int id) {
        return films.get(id);
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(id);
        id++;
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        films.replace(film.getId(), film);
        return film;
    }

    public Film deleteLike(int filmId, int userId) {
        films.get(filmId).deleteLike(userId);
        return films.get(filmId);
    }

    public List<Integer> findFilmsId() {
        return new ArrayList<>(films.keySet());
    }
}
