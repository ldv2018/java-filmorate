package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.*;

@Component
public class InMemoryFilmStorage implements Storage<Film> {

    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> find(int id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public List<Integer> findId() {
        return new ArrayList<>(films.keySet());
    }

    @Override
    public Film add(Film film) {
        film.setId(id);
        id++;
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        films.replace(film.getId(), film);
        return film;
    }

    public boolean isAlreadyExist(int id) {
        return films.containsKey(id);
    }
}
