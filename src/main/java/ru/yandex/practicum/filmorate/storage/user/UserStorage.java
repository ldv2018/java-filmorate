package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> findAll();

    User addUser(User user);

    User updateUser(User user);

    List<Integer> findUsersId();

    User findUserById(int id);

}
