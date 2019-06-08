package com.example.demo.payload;

public class JwtLoginSuccessResponse {

    private boolean success;
    private String token;

    public JwtLoginSuccessResponse(boolean success, String token) {
        this.success = success;
        this.token = token;
    }

    @Override
    public String toString() {
        return "JwtLoginSuccessResponse{" +
                "success=" + success +
                ", token='" + token + '\'' +
                '}';
    }
}
