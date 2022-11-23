package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.SaveExeption;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class UserController {

    private final HashMap<Integer, User> users = new HashMap<>();
    private int id = 1;
    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/users")
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping(path = "/users")
    public User addUser(@RequestBody User user) {
        if (user == null) {
            throw new SaveExeption("user save failed");
        }
        UserValidator.validateUser(user);
        user.setId(id);
        users.put(id, user);
        log.info("user added");
        id++;
        return user;
    }

    @PutMapping(path = "/users")
    public User updateFilm(@RequestBody User user) {
        if (user == null || !users.containsKey(user.getId())) {
            throw new SaveExeption("user update failed");
        }
        UserValidator.validateUser(user);
        users.replace(user.getId(), user);
        log.info("user updated");
        return user;
    }
}
