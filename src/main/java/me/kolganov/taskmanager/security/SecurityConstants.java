package me.kolganov.taskmanager.security;

public class SecurityConstants {
    public final static String SECRET = "SecretKeyForJWTs";
    public final static String TOKEN_PREFIX = "Bearer ";
    public final static String HEADER_STRING = "Authorization";
    public final static long EXPIRATION_TIME = 3000000; //50 min
}
