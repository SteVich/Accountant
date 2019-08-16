package com.example.biblio.controller;

import com.example.biblio.model.Book;
import com.example.biblio.payload.ApiResponse;
import com.example.biblio.payload.modelRequest.BookRequest;
import com.example.biblio.service.BookService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BookController {

    BookService bookService;

    @GetMapping
    public ResponseEntity<List<Book>> findAllBook() {
        return ResponseEntity.ok(bookService.findAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> findByBookId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(bookService.findByBookId(id));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse> createBook(@Valid @RequestBody BookRequest bookRequest) {
        bookService.createBook(bookRequest);

        return ResponseEntity.ok().body(new ApiResponse(true, "Book created successfully"));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateBook(@PathVariable("id") Long id, @Valid @RequestBody BookRequest bookRequest) {
        bookService.updateBook(id, bookRequest);

        return ResponseEntity.ok().body(new ApiResponse(true, "Book updated successfully"));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id);

        return ResponseEntity.noContent().build();
    }

}