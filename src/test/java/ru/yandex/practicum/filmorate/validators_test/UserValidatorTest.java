package ru.yandex.practicum.filmorate.validators_test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserValidatorTest {

    User user = new User("info@info.net", "login", "name", LocalDate.now());

    @Test
    public void wrongMail() {
        user.setEmail("");
        assertThrows(ValidationException.class, () -> {
            UserValidator.validateUser(user);
        });

        user.setEmail("info.net");
        assertThrows(ValidationException.class, () -> {
            UserValidator.validateUser(user);
        });


    }

    @Test
    public void wrongLogin() {
        user.setLogin("");
        assertThrows(ValidationException.class, () -> {
            UserValidator.validateUser(user);
        });

        user.setLogin("  ");
        assertThrows(ValidationException.class, () -> {
            UserValidator.validateUser(user);
        });
    }

    @Test
    public void blankName() {
        user.setName("");
        UserValidator.validateUser(user);
        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    public void futureBirthday() {
        user.setBirthday(LocalDate.MAX);
        assertThrows(ValidationException.class, () -> {
            UserValidator.validateUser(user);
        });
    }

}
