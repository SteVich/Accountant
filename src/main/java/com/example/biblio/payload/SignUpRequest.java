package com.example.biblio.payload;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SignUpRequest {

    @NotBlank
    @Size(min = 2, max = 40)
    @Column(name = "name")
    private String name;

    @NotBlank
    @Size(min = 2, max = 15)
    @Column(name = "username")
    private String username;

    @NotBlank
    @Size(max = 40)
    @Column(name = "email")
    @Email
    private String email;

    @NotBlank
    @Size(min = 4, max = 20)
    @Column(name = "password")
    private String password;

}
