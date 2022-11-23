package ru.yandex.practicum.filmorate.exceptions;

public class ValidationException extends RuntimeException{

    public ValidationException() {}

    public ValidationException(String str) {
        super(str);
    }
}
