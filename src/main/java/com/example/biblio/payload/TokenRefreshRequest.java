package com.example.biblio.payload;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import javax.validation.constraints.NotBlank;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenRefreshRequest {

    @NotBlank(message = "Refresh token cannot be blank")
    String refreshToken;

}
