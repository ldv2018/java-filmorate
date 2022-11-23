package ru.yandex.practicum.filmorate.validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmValidator {
    private final static LocalDate FILM_RELEASE = LocalDate.of(1895, 12, 28);
    private final static Logger log = LoggerFactory.getLogger(FilmValidator.class);

    public static void validateFilm(Film film) {
        if (film.getName().isBlank()) {
            log.info("validation exception: film name is blank");
            throw new ValidationException("Film name is blank");
        }

        if (film.getDescription().length() > 200) {
            log.info("validation exception: description length");
            throw new ValidationException("Description length out of index");
        }

        if (film.getReleaseDate().isBefore(FILM_RELEASE)) {
            log.info("validation exception: film release date");
            throw new ValidationException("Film release date is before required");
        }

        if (film.getDuration() < 0) {
            log.info("validation exception: film duration");
            throw new ValidationException("Film duration is negative");
        }
    }
}
