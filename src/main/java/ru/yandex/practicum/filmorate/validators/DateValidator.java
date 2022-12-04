package ru.yandex.practicum.filmorate.validators;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateValidator implements ConstraintValidator<ValidateDate, LocalDate> {

    private final static LocalDate FILM_RELEASE = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value.isBefore(FILM_RELEASE)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Film release date is before required");
        }
        return true;
    }
}
