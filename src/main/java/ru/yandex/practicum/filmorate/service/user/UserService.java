package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    public UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int id, int friendId) {
        checkId(id, friendId);
        userStorage.getUser(id).addFriend(friendId);
        userStorage.getUser(friendId).addFriend(id);
    }

    public void deleteFriend(int id, int friendId) {
        checkId(id, friendId);
        userStorage.getUser(id).deleteFriend(friendId);
        userStorage.getUser(friendId).deleteFriend(id);
    }

    public List<User> getFriends(int id) {
        if (!userStorage.getUsersId().contains(id)) {
            throw new RuntimeException("User with id = " + id + " not found");
        }
        Set<Integer> userFriends = userStorage.getUser(id).getFriends();
        List<User> users = new ArrayList<>();
        for (Integer i : userFriends) {
            users.add(userStorage.getUser(i));
        }
        return users;
    }

    private void checkId(int id, int friendId) {
        if (!userStorage.getUsersId().contains(id)) {
            throw new RuntimeException("User with id = " + id + " not found");
        }
        if (!userStorage.getUsersId().contains(friendId)) {
            throw new RuntimeException("User with id = " + friendId + " not found");
        }
    }
}
