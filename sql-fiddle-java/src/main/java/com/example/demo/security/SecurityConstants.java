package com.example.demo.security;

public class SecurityConstants {

    public static final String SING_UP_URLS = "/**";
    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final long EXPIRATION_TIME = 300_000; // 30 seconds
}