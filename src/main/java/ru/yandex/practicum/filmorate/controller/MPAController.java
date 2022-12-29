package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.mpa.MPAService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MPAController {

    private final MPAService mpaService;

    @Autowired
    public MPAController(MPAService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MPA> get() {
        return mpaService.getAll();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public MPA get(@PathVariable int id) {
        if (id <= 0) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "MPA id must be positive");
        }
        return mpaService.get(id);
    }
}
