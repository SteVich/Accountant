package com.example.biblio.dto;

import com.example.biblio.model.Book;
import com.example.biblio.model.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BorrowedDTO {

    Long id;

    User user;

    Book book;
}
