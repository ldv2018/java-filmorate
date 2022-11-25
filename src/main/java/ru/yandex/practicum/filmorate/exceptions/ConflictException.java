package ru.yandex.practicum.filmorate.exceptions;

public class ConflictException extends RuntimeException{

    public ConflictException (String msg) {
        super(msg);
    }
}
