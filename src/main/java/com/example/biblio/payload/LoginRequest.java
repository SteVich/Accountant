package com.example.biblio.payload;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter

public class LoginRequest {

    @NotBlank
    private String usernameOrEmail;

    @NotBlank
    private String password;
}
