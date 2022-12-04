package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage{

    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User addUser(User user) {
        user.setId(id);
        id++;
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.replace(user.getId(), user);
        return user;
    }

    @Override
    public List<Integer> findUsersId() {
        return new ArrayList<>(users.keySet());
    }

    @Override
    public User findUserById(int id) {
        return users.get(id);
    }
}
