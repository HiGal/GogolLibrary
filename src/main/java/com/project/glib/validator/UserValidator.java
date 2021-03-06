package com.project.glib.validator;

import com.project.glib.dao.implementations.UserDaoImplementation;
import com.project.glib.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    @Autowired
    private UserDaoImplementation usersDao;

    /**
     * Check is it user
     *
     * @param aClass class of object
     * @return true if class equals User, false otherwise
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    /**
     * Check errors in input data for user
     *
     * @param o      user model
     * @param errors model of errors
     */
    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "login", "NotEmpty");

        if (user.getLogin().length() < 6 || user.getLogin().length() > 32) {
            errors.rejectValue("login", "Size.userForm.login");
        }

        try {
            if (usersDao.findByLogin(user.getLogin()) != null) {
                errors.rejectValue("login", "Duplicate.userForm.login");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.userForm.password");
        }

    }
}
