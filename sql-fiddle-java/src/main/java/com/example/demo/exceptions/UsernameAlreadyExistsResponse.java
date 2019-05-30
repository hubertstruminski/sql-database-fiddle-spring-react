package com.example.demo.exceptions;

public class UsernameAlreadyExistsResponse {

    private String userName;

    public UsernameAlreadyExistsResponse(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
