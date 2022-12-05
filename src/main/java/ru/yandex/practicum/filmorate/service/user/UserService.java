package ru.yandex.practicum.filmorate.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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
        User userId = userStorage.find(id).orElseThrow(() ->
                new NotFoundException(HttpStatus.NOT_FOUND, "Bad id " + id + ". No user found"));
        User userFriendId = userStorage.find(friendId).orElseThrow(() ->
                new NotFoundException(HttpStatus.NOT_FOUND, "Bad id " + friendId + ". No user found"));
        userId.addFriend(friendId);
        userFriendId.addFriend(id);
        return id + " and " + friendId + " are friends now";
    }

    public String deleteFriend(int id, int friendId) {
        User userId = userStorage.find(id).orElseThrow(() ->
                new NotFoundException(HttpStatus.NOT_FOUND, "Bad id " + id + ". No user found"));
        User userFriendId = userStorage.find(friendId).orElseThrow(() ->
                new NotFoundException(HttpStatus.NOT_FOUND, "Bad id " + friendId + ". No user found"));
        userId.deleteFriend(friendId);
        userFriendId.deleteFriend(id);
        log.info("delete friend (id " + id + " & " + friendId +") --OK");
        return id + " unfriended " + friendId;
    }

    public List<User> getFriends(int id) {
        User userId = userStorage.find(id).orElseThrow(() ->
                new NotFoundException(HttpStatus.NOT_FOUND, "Bad id " + id + ". No user found"));
        Set<Integer> friends = userId.getFriends();
        return getUsersById(friends);
    }

    public List<User> getCommonFriends(int id, int otherId) {
        User userId = userStorage.find(id).orElseThrow(() ->
                new NotFoundException(HttpStatus.NOT_FOUND, "Bad id " + id + ". No user found"));
        User userOtherId = userStorage.find(otherId).orElseThrow(() ->
                new NotFoundException(HttpStatus.NOT_FOUND, "Bad id " + otherId + ". No user found"));
        throwIfUserHaveNoFriends(userId);
        throwIfUserHaveNoFriends(userOtherId);
        Set<Integer> commonFriends = new TreeSet<>(userId.getFriends());
        Set<Integer> commonFriends2 = userOtherId.getFriends();
        commonFriends.retainAll(commonFriends2);
        log.info("return common friends list --OK");
        return getUsersById(commonFriends);
    }

    private void throwIfUserHaveNoFriends(User user) {
        if (user.getFriends() == null) {
            throw new ConflictException(HttpStatus.NOT_FOUND, "User with id "
                    + user.getId() + " has no friend");
        }
        log.info("is have friends (id=" + user.getId() + ") --OK");
    }

    private List<User> getUsersById(Set<Integer> usersId) {
        List<User> users = new ArrayList<>();
        for (int i : usersId) {
            users.add(userStorage.find(i).orElseThrow(() ->
                    new NotFoundException(HttpStatus.NOT_FOUND, "Bad id. No user found")));
        }
        return users;
    }

    private void throwIfUserNotValid(int id) {
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
