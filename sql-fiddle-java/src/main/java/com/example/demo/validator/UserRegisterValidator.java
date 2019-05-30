package com.example.demo.validator;

import com.example.demo.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Component
public class UserRegisterValidator implements Validator {

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        if(!user.getPassword().equals(user.getMatchingPassword())) {
            errors.rejectValue("matchingPassword", "Match", "Passwords must mach");
        }
    }
}
