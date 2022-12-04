package ru.yandex.practicum.filmorate.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.ConflictException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        if (user == null) {
            throw new BadRequestException(HttpStatus.NOT_FOUND, "Bad request. User couldn't be null");
        }
        replaceEmptyName(user);
        log.info("add user --OK");
        return userStorage.addUser(user);
    }

    public List<User> getUsers() {
        log.info("return users list --OK");
        return userStorage.findAll();
    }

    public User updateUser(User user) {
        if (user == null) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Bad request. User couldn't be null");
        }
        checkId(user.getId());
        replaceEmptyName(user);
        log.info("update user (id " + user.getId() + ") --OK");
        return userStorage.updateUser(user);
    }

    public User getUserById(int id) {
        log.info("return user (id " + id + ") --OK");
        return userStorage.findUserById(id);
    }

    public String addFriend(int id, int friendId) {
        checkId(id);
        checkId(friendId);
        userStorage.findUserById(id).addFriend(friendId);
        userStorage.findUserById(friendId).addFriend(id);
        return id + " and " + friendId + " are friends now";
    }

    public String deleteFriend(int id, int friendId) {
        checkId(id);
        checkId(friendId);
        userStorage.findUserById(id).deleteFriend(friendId);
        userStorage.findUserById(friendId).deleteFriend(id);
        log.info("delete friend (id " + id + " & " + friendId +") --OK");
        return id + " unfriended " + friendId;
    }

    public List<User> getFriends(int id) {
        checkId(id);
        Set<Integer> friends = userStorage.findUserById(id).getFriends();
        return getUsersById(friends);
    }

    public List<User> getCommonFriends(int id, int otherId) {
        checkId(id);
        checkId(otherId);
        User userId = userStorage.findUserById(id);
        User userOtherId = userStorage.findUserById(otherId);
        Set<Integer> commonFriends = userId.getFriends();
        Set<Integer> commonFriends2 = userOtherId.getFriends();
        Set<Integer> result = new TreeSet<>();
        for (int i : commonFriends) {
            for (int j : commonFriends2) {
                if (i == j) {
                    result.add(i);
                }
            }
        }
        log.info("return common friends list --OK");
        return getUsersById(result);
    }

    private void checkFriends(int id) {
        if (userStorage.findUserById(id).getFriends() == null) {
            throw new ConflictException(HttpStatus.NOT_FOUND, "User with id "
                    + id + " has no friend");
        }
        log.info("is have friends (id=" + id + ") --OK");
    }
    private List<User> getUsersById(Set<Integer> usersId) {
        List<User> users = new ArrayList<>();
        for (int i : usersId) {
            users.add(userStorage.findUserById(i));
        }
        return users;
    }

    private void checkId(int id) {
        if (!userStorage.findUsersId().contains(id)) {
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
