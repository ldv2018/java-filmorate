package ru.yandex.practicum.filmorate.exceptions;

public class SaveExeption extends RuntimeException{

    public SaveExeption() {

    }

    public SaveExeption(String msg) {
        super(msg);
    }
}
