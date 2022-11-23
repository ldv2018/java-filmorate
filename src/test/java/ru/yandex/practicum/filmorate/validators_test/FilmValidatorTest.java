package ru.yandex.practicum.filmorate.validators_test;

import org.junit.jupiter.api.Test;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.LocalDate;

@SpringBootTest
public class FilmValidatorTest {

    Film film = new Film("filmName", "filmDescription", LocalDate.now(), Duration.ofMinutes(90));

    @Test
    public void blankName() {
        film.setName("");
        assertThrows(ValidationException.class, () -> {
            FilmValidator.validateFilm(film);
        });
    }

    @Test
    public void bigDescription() {
        String description = StringUtils.repeat("a", 201);
        film.setDescription(description);
        assertThrows(ValidationException.class, () -> {
            FilmValidator.validateFilm(film);
        });
    }

    @Test
    public void releaseDate() {
        film.setReleaseDate(LocalDate.MIN);
        assertThrows(ValidationException.class, () -> {
            FilmValidator.validateFilm(film);
        });
    }

    @Test
    public void negativeDuration() {
        film.setDuration(Duration.ofMinutes(-9));
        assertThrows(ValidationException.class, () -> {
            FilmValidator.validateFilm(film);
        });
    }
}
