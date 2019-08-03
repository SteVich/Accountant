package com.example.biblio.payload.modelRequest;

import com.example.biblio.model.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookRequest {

    @NotBlank
    @Size(min = 1, max = 100)
    String title;

    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "\"yyyy-MM-dd\"")
    Date releaseTime;

    @NotNull
    User user;
}