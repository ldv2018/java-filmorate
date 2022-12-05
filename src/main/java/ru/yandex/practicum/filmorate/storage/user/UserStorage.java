package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    List<User> findAll();

    User add(User user);

    User update(User user);

    List<Integer> findUsersId();

    Optional<User> find(int id);

}
