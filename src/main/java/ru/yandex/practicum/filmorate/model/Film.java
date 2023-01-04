package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.filmorate.validators.ValidateDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@SuperBuilder
public class Film {

    private int id;
    @NotNull
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @ValidateDate
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private final List<Genre> genres = new ArrayList<>();
    private Mpa mpa;
    private final Set<Integer> likes = new HashSet<>();

    public void addGenres(Genre genre) {
        genres.add(genre);
    };
}
