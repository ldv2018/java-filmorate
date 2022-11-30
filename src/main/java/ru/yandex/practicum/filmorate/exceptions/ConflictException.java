package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;

public class ConflictException extends RuntimeException{

    public ConflictException (HttpStatus notFound, String msg) {
        super(msg);
    }
}
