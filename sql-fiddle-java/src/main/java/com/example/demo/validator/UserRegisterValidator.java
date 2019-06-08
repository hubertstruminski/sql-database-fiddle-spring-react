package com.example.demo.validator;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Component
public class UserRegisterValidator implements Validator {

    @Autowired
    private UserService userService;

    @NotNull(message = "user name is required")
    @Size(min = 2, message = "user name must have at least 2 characters")
    private String userName;

    @NotNull(message = "password is required")
    @Size(min = 6, message = "password must have at least 6 characters")
    private String password;

    @NotNull(message = "password is required")
    @Size(min = 6, message = "password must have at least 6 characters")
    private String matchingPassword;

    @NotNull(message = "first name is required")
    @Size(min = 2, message = "first name must have at least 2 characters")
    private String firstName;

    @NotNull(message = "last name is required")
    @Size(min = 2, message = "last name must have at least 2 characters")
    private String lastName;

    @NotNull(message = "email is required")
    @Email(message = "Given string is not an email")
    private String email;

    public UserRegisterValidator() {

    }

    public UserRegisterValidator(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public UserRegisterValidator(String userName, String password, String matchingPassword, String firstName, String lastName, String email) {
        this.userName = userName;
        this.password = password;
        this.matchingPassword = matchingPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = userService.converFromUserValidator((UserRegisterValidator) o);

        if(!user.getPassword().equals(user.getMatchingPassword())) {
            errors.rejectValue("matchingPassword", "Match", "Passwords must mach");
        }
    }
}
