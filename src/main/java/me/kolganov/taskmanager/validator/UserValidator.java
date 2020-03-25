package me.kolganov.taskmanager.validator;

import me.kolganov.taskmanager.domain.AppUser;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return AppUser.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        AppUser user = (AppUser) o;

        if (user.getPassword() == null) {
            errors.rejectValue("password", "Length", "Password must be not null");
            return;
        }

        if (user.getPassword().length() < 6) {
            errors.rejectValue("password", "Length", "Password must be at least 6 characters");
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "Match", "Passwords must match");
        }
    }
}
