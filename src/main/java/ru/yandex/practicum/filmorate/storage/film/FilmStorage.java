package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> findAll();

    Film findFilmById(int id);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film deleteLike(int filmId, int userId);

    List<Integer> findFilmsId();
}