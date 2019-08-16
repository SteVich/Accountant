package com.example.biblio.dto;

import com.example.biblio.model.Borrowed;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookDTO {

    Long id;

    String title;

    LocalDate releaseTime;

    List<Borrowed> borroweds;

    Boolean access;

}
