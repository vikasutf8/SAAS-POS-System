package com.pm.saaspossystem.configurations;

public class JwtConstant {

    public static final String SECRET_KEY = "mySuperSecretKeyForJwtSigning123456";

    public static final String JWT_HEADER = "Authorization";

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour
}
