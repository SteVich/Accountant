package com.example.biblio.util;

public class JwtProperties {

    public static final long JWT_EXPIRATION_MS = 3600000;
    public static final String JWT_SECRET = "SeCreTKey";
    public static final long REFRESH_TOKEN_EXPIRATION = 30 * 24 * 60 * 60;
    public static final String TOKEN_TYPE = "Bearer";

}
