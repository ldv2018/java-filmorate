package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.*;

@Component
public class InMemoryUserStorage implements Storage<User> {

    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User add(User user) {
        user.setId(id);
        id++;
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        users.replace(user.getId(), user);
        return user;
    }

    @Override
    public List<Integer> findId() {
        return new ArrayList<>(users.keySet());
    }

    @Override
    public boolean isAlreadyExist(int id) {
        return users.containsKey(id);
    }

    @Override
    public Optional<User> find(int id) {
        return Optional.ofNullable(users.get(id));
    }
}
