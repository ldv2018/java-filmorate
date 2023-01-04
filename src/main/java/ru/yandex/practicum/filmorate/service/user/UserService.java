package ru.yandex.practicum.filmorate.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User add(User user) {
        replaceEmptyName(user);
        log.info("add user --OK");

        return userStorage.add(user);
    }

    public List<User> get() {
        log.info("return users list --OK");

        return userStorage.findAll();
    }

    public User update(User user) {
        throwIfUserNotValid(user.getId());
        replaceEmptyName(user);
        log.info("update user (id " + user.getId() + ") --OK");

        return userStorage.update(user);
    }

    public User get(int id) {
        log.info("return user (id " + id + ") --OK");

        return userStorage.find(id).orElseThrow(() ->
                        new NotFoundException(HttpStatus.NOT_FOUND, "Bad id " + id + ". No user found"));
    }

    public String addFriend(int id, int friendId) {
        throwIfUserNotValid(id);
        throwIfUserNotValid(friendId);
        userStorage.addFriend(id, friendId);

        return id + " and " + friendId + " are friends now";
    }

    public String deleteFriend(int id, int friendId) {
        throwIfUserNotValid(id);
        throwIfUserNotValid(friendId);
        userStorage.deleteFriend(id, friendId);
        log.info("delete friend (id " + id + " & " + friendId +") --OK");

        return id + " unfriended " + friendId;
    }

    public List<User> getFriends(int id) {
        throwIfUserNotValid(id);

        return userStorage.getFriends(id);
    }

    public List<User> getCommonFriends(int id, int otherId) {
        throwIfUserNotValid(id);
        throwIfUserNotValid(otherId);
        log.info("return common friends list --OK");

        return userStorage.getCommonFriends(id, otherId);
    }

    private void throwIfUserNotValid(int id) {
        if (!userStorage.isAlreadyExist(id)) {
            log.info("check id fail");
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Bad id " + id + ". No user found");
        }
    }

    private void replaceEmptyName(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("replace empty name --DONE");
        }
    }
}
