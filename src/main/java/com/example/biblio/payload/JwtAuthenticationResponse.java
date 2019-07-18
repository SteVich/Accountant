package com.example.biblio.payload;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class JwtAuthenticationResponse {

    @Column(name = "accessToken")
    private String accessToken;

    @Column(name = "tokenType")
    private String tokenType = "Bearer";

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }

}
