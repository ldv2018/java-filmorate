package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage{

    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(int id) {
        return films.get(id);
    }

    @Override
    public Film addFilm(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        films.replace(film.getId(), film);
        return film;
    }

    public Film addLike(int filmId, int userId) {
        films.get(filmId).addLike(userId);
        return films.get(filmId);
    }

    public Film deleteLike(int filmId, int userId) {
        films.get(filmId).deleteLike(userId);
        return films.get(filmId);
    }

    public List<Integer> getFilmsId() {
        return new ArrayList<>(films.keySet());
    }
}
