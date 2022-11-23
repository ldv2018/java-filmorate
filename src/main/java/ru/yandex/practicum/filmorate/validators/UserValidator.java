package ru.yandex.practicum.filmorate.validators;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserValidator {

    public static void validateUser(User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("E-mail wrong format");
        }

        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Login wrong format");
        }

        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("date of birth cannot be in the future");
        }
    }
}
