package ru.yandex.practicum.filmorate.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.exceptions.ConflictException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private static Logger log = LoggerFactory.getLogger(UserService.class);
    public UserStorage userStorage;
    private int id = 1;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        if (user == null) {
            throw new BadRequestException(HttpStatus.NOT_FOUND, "Bad request. User couldn't be null");
        }
        user.setId(id);
        id++;
        replaceEmptyLogin(user);
        return userStorage.addUser(user);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User updateUser(User user) {
        if (user == null) {
            throw new BadRequestException(HttpStatus.NOT_FOUND, "Bad request. User couldn't be null");
        }
        if (!userStorage.getUsersId().contains(user.getId())) {
            throw new ConflictException(HttpStatus.NOT_FOUND, "Bad id. No user with id = " + user.getId());
        }
        replaceEmptyLogin(user);
        return userStorage.updateUser(user);
    }

    public User getUserById(int id) {
        checkId(id);
        return userStorage.getUser(id);
    }

    public String addFriend(int id, int friendId) {
        checkId(id);
        checkId(friendId);
        userStorage.getUser(id).addFriend(friendId);
        userStorage.getUser(friendId).addFriend(id);
        return userStorage.getUser(id).getName() + " and "
                + userStorage.getUser(friendId).getName() + " are friends now";
    }

    public String deleteFriend(int id, int friendId) {
        checkId(id);
        checkId(friendId);
        userStorage.getUser(id).deleteFriend(friendId);
        userStorage.getUser(friendId).deleteFriend(id);
        return userStorage.getUser(id).getName() + " unfriended "
                + userStorage.getUser(friendId).getName();
    }

    public List<User> getFriends(int id) {
        checkId(id);
        Set<Integer> userFriends = userStorage.getUser(id).getFriends();
        return getUsersById(userFriends);
    }

    public List<User> getCommonFriends(int id, int otherId) {
        checkId(id);
        checkId(otherId);
        checkFriends(id);
        checkFriends(otherId);
        Set<Integer> commonFriends = userStorage.getUser(id).getFriends();
        commonFriends.retainAll(userStorage.getUser(otherId).getFriends());
        return getUsersById(commonFriends);
    }

    private void checkFriends(int id) {
        if (userStorage.getUser(id).getFriends() == null) {
            throw new ConflictException(HttpStatus.NOT_FOUND, "User with id "
                    + id + " has no friend");
        }
    }
    private List<User> getUsersById(Set<Integer> usersId) {
        List<User> users = new ArrayList<>();
        for (Integer i : usersId) {
            users.add(userStorage.getUser(i));
        }
        return users;
    }

    private void checkId(int id) {
        if (!userStorage.getUsersId().contains(id)) {
            throw new BadRequestException(HttpStatus.NOT_FOUND, "User with id = " + id + " not found");
        }
    }

    private void replaceEmptyLogin(User user) {
        if (user.getLogin().isBlank()) {
            user.setLogin(user.getName());
            log.info("Login was empty. Replace empty login.");
        }
    }
}
