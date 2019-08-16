package com.example.biblio.payload.modelRequest;

import com.example.biblio.model.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookRequest {

    @NotBlank
    @Size(min = 1, max = 100)
    String title;

    @NotNull
    LocalDate releaseTime;

    @NotNull
    User user;
}