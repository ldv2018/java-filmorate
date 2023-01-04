package ru.yandex.practicum.filmorate.service.mpa;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;
    private final Logger log = LoggerFactory.getLogger(MpaService.class);

    public List<Mpa> getAll() {
        log.info("get MPAs --OK");

        return mpaStorage.findAll();
    }

    public Mpa get(int id) {
        log.info("get MPA by id --OK");

        return mpaStorage.find(id).orElseThrow(() ->
                new NotFoundException(HttpStatus.NOT_FOUND, "MPA id "+ id +" not found"));
    }
}