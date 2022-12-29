package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    List<Film> findAll();

    Film add(Film film);

    Film update(Film film);

    Optional<Film> find(int id);

    List<Integer> findId();

    boolean isAlreadyExist(int id);

    Optional<Film> addLike(int filmId, int userId);

    Optional<Film> deleteLike(int filmId, int userId);

    List<Film> getPopularFilms(int count);
}
