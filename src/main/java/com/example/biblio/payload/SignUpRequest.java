package com.example.biblio.payload;

import com.example.biblio.validation.ValidPassword;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignUpRequest {

    @NotBlank
    @Size(min = 2, max = 40)
    String name;

    @NotBlank
    @Size(min = 2, max = 15)
    String username;

    @NotBlank
    @Size(max = 40)
    @Email
    String email;

    @NotBlank
    @ValidPassword
    String password;

}
