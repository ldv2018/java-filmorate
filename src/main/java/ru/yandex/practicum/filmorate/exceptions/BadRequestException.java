package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends RuntimeException{

    public BadRequestException(HttpStatus status, String msg) {
        super(msg);
    }
}
