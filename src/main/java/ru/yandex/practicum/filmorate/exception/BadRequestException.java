package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends RuntimeException{

    public BadRequestException(HttpStatus status, String msg) {
        super(msg);
    }
}
