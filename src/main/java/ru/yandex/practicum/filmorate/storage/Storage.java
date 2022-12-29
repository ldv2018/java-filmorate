package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface Storage<T> {
    List<T> findAll();

    T add(T t);

    T update(T t);

    Optional<T> find(int id);

    List<Integer> findId();

    boolean isAlreadyExist(int id);

    void addFriend(int id, int friendId);

    void deleteFriend(int id, int friendId);

    List<T> getFriends(int id);

    List<User> getCommonFriends(int id, int otherId);


}
