package ru.yandex.practicum.filmorate.service.genre;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.DBGenreStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final DBGenreStorage genreStorage;
    private final Logger log = LoggerFactory.getLogger(GenreService.class);

    public List<Genre> get() {
        log.info("get genres --OK");
        return genreStorage.findAll();
    }

}
