package com.example.biblio.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * Use this variable for set expiration time of access token
     */
    Long expirationAccessToken;

    /**
     * Use this variable for set expiration time of refresh token
     */
    Long expirationRefreshToken;

    /**
     * Use this variable for set key that allow to validate the JWT
     */
    String secretKey;

}
