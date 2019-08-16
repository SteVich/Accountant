package com.example.biblio.transfer;

import com.example.biblio.dto.BookDTO;
import com.example.biblio.model.Book;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDTO toBookDTO(Book book);

    List<BookDTO> toBookDTOs(List<Book> books);

    Book toBook(BookDTO bookDTO);

}