package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GenreStorage {

    List<Genre> findAll();

    Optional<Genre> find(int id);

    List<Genre> findByFilmId(int filmId);

    Map<Integer, List<Genre>> findAllToFilm();
}
