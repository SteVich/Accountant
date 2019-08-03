package com.example.biblio.dto;

import com.example.biblio.model.Role;
import com.example.biblio.payload.modelRequest.BookRequest;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {

    @NotEmpty
    Long id;

    @NotNull
    String name;

    @NotNull
    String username;

    @NotNull
    String email;

    @NotNull
    Set<Role> roles;

    List<BookRequest> books;

}
