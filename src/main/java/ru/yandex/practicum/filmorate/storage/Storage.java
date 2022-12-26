package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Optional;

public interface Storage<T> {
    List<T> findAll();

    T add(T t);

    T update(T t);

    Optional<T> find(int id);

    List<Integer> findId();

    boolean isAlreadyExist(int id);
}
