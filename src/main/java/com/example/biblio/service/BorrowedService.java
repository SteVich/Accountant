package com.example.biblio.service;

import com.example.biblio.exception.BookAccessException;
import com.example.biblio.exception.ResourceNotFoundException;
import com.example.biblio.exception.UserNotFoundException;
import com.example.biblio.model.Book;
import com.example.biblio.model.Borrowed;
import com.example.biblio.model.User;
import com.example.biblio.repository.BookRepository;
import com.example.biblio.repository.BorrowedRepository;
import com.example.biblio.repository.UserRepository;
import com.example.biblio.security.UserPrincipal;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BorrowedService {

    BorrowedRepository borrowedRepository;
    UserRepository userRepository;
    BookRepository bookRepository;

    @Transactional(readOnly = true)
    public List<Borrowed> findAllBorroweds() {
        return borrowedRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Borrowed findBorrowedById(Long id) {
        return borrowedRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Borrowed", "id", id));
    }

    @Transactional
    public Borrowed createBorrowed(Long bookId) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new UserNotFoundException("User not found with id with: " + userPrincipal.getId()));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book", "id", bookId));

        Borrowed borrowed = new Borrowed();
        if (book.getAccess()) {
            throw new BookAccessException(book.getTitle());
        } else {
            borrowed.setUser(user);
            borrowed.setBook(book);

            book.setAccess(true);
            bookRepository.save(book);
        }

        return borrowedRepository.save(borrowed);
    }

    @Transactional
    public void deleteBorrowed(Long id) {
        borrowedRepository.deleteById(id);
    }

}