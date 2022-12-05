package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    List<Film> findAll();

    Optional<Film> find(int id);

    Film add(Film film);

    Film update(Film film);

    boolean isAlreadyExist(int id);
}