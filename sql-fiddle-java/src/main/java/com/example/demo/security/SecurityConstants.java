package com.example.demo.security;

public class SecurityConstants {

    public static final String SING_UP_URL = "/**";
    public static final String SECRET_KEY = "SecretKeyToGenJWTs";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";
    public static final long EXPIRATION_TIME = 300_000; // 30 seconds
}
