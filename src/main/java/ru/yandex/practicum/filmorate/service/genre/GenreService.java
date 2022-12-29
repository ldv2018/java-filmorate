package ru.yandex.practicum.filmorate.service.genre;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;
    private final Logger log = LoggerFactory.getLogger(GenreService.class);

    public List<Genre> getAll() {
        log.info("get genres --OK");
        return genreStorage.findAll();
    }

    public Genre get(int id) {
        log.info("get genre by id --OK");
        return genreStorage
                .find(id)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND,
                        "genre with id " + id + "not found"));
    }

    public List<Integer> getByFilm(int id) {
        log.info("get genres by film --OK");
        return genreStorage.findByFilm(id);
    }
}