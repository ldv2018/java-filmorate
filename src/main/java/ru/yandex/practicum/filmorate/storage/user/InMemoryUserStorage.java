package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {

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

    @Override
    public void addFriend(int id, int friendId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<User> getFriends(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<User> getCommonFriends(int id, int otherId) {
        throw new UnsupportedOperationException();
    }
}
