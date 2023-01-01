package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.film.DBFilmStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmStorageTest {
    private final DBFilmStorage filmStorage;

    @Test
    void findValidFilm() {
        Film testFilm = Film.builder()
                .name("name")
                .description("description")
                .duration(150)
                .releaseDate(LocalDate.now())
                .mpa(MPA.builder().id(1).build())
                .build();

        int filmId = filmStorage.add(testFilm).getId();

        Optional<Film> filmOptional = filmStorage.find(filmId);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", filmId)
                );
    }
}
