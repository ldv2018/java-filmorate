package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage{

    private static final Logger log = LoggerFactory.getLogger(InMemoryUserStorage.class);
    private Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User addUser(User user) {
        replaceEmptyLogin(user);
        user.setId(id);
        users.put(id, user);
        id++;
        log.info("user added");
        return user;
    }

    @Override
    public User updateUser(User user) {
        replaceEmptyLogin(user);
        if (!users.containsKey(user.getId())) {
            throw new RuntimeException(); //поменяй исключение
        }
        users.replace(user.getId(), user);
        log.info("user updated");
        return user;
    }

    @Override
    public List<Integer> getUsersId() {
        return new ArrayList<>(users.keySet());
    }

    @Override
    public User getUser(int id) {
        return users.get(id);
    }

    private void replaceEmptyLogin(User user) {
        if (user.getLogin().isBlank()) {
            user.setLogin(user.getName());
        }
    }
}
