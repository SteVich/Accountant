package com.example.biblio.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserNotFoundException extends RuntimeException {

    Long id;
    String usernameOrEmail;

    public UserNotFoundException(Long id) {
        super("User: " + id + " not found.");
        this.id = id;
    }

    public UserNotFoundException(String usernameOrEmail) {
        super("User not found with username or email with : " + usernameOrEmail);
        this.usernameOrEmail = usernameOrEmail;
    }

}