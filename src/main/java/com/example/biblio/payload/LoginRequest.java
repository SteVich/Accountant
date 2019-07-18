package com.example.biblio.payload;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginRequest {

    @NotBlank
    @Column(name = "usernameOrEmail")
    private String usernameOrEmail;

    @NotBlank
    @Column(name = "password")
    private String password;

}
