package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends RuntimeException{

    public ConflictException (HttpStatus status, String msg) {
        super(msg);
    }
}
