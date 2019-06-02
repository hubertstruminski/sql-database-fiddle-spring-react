package com.example.demo.exceptions;

public class InvalidLoginResponse {

    private String userName;
    private String password;

    public InvalidLoginResponse() {
        this.userName = "Invalid userName";
        this.password = "Invalid password";
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
}
