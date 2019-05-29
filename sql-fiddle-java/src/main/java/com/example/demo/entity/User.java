package com.example.demo.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "user_name")
    @NotNull(message = "user name is required")
    @Size(min = 2, message = "user name must have at least 2 characters")
    private String userName;

    @Column(name = "password")
    @NotNull(message = "password is required")
    @Size(min = 6, message = "password must have at least 6 characters")
    private String password;

    @Transient
    @NotNull(message = "password is required")
    @Size(min = 6, message = "password must have at least 6 characters")
    private String matchingPassword;

    @Column(name = "first_name")
    @NotNull(message = "first name is required")
    @Size(min = 2, message = "first name must have at least 2 characters")
    private String firstName;

    @Column(name = "last_name")
    @NotNull(message = "last name is required")
    @Size(min = 2, message = "last name must have at least 2 characters")
    private String lastName;

    @Column(name = "email")
    @NotNull(message = "email is required")
    @Email(message = "Given string is not an email")
    private String email;

    @Column(name = "is_active")
    private boolean isActive;

    public User() {

    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public User(String userName, String password, String firstName, String lastName, String email, boolean isActive) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isActive = isActive;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
