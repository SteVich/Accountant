package com.example.biblio.dto;

import com.example.biblio.model.Borrowed;
import com.example.biblio.model.Role;
import com.example.biblio.payload.modelRequest.BookRequest;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.util.List;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {

    Long id;

    String name;

    String username;

    String email;

    Set<Role> roles;

    List<BookRequest> books;

    List<Borrowed> borroweds;

}
