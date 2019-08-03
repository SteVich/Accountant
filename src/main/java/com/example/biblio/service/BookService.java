package com.example.biblio.service;

import com.example.biblio.exception.ResourceNotFoundException;
import com.example.biblio.model.Book;
import com.example.biblio.payload.modelRequest.BookRequest;
import com.example.biblio.repository.BookRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BookService {

    BookRepository bookRepository;

    @Transactional(readOnly = true)
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    @Transactional
    public Book findByBookId(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
    }

    @Transactional
    public Book createBook(BookRequest bookRequest) {
        Book book = new Book();
        book.setTitle(bookRequest.getTitle());
        book.setReleaseTime(bookRequest.getReleaseTime());
        book.setAccess(false);

        return bookRepository.save(book);
    }

    @Transactional
    public Book updateBook(Long id, BookRequest bookRequest) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));

        book.setId(id);
        book.setTitle(bookRequest.getTitle());
        book.setReleaseTime(bookRequest.getReleaseTime());

        return bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

}