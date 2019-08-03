package com.example.biblio.service;

import com.example.biblio.exception.UserNotFoundException;
import com.example.biblio.model.Borrowed;
import com.example.biblio.model.User;
import com.example.biblio.repository.BorrowedRepository;
import com.example.biblio.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserService {

    UserRepository userRepository;
    BorrowedRepository borrowedRepository;

    @Transactional(readOnly = true)
    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id with: " + id));
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


    @Transactional(readOnly = true)
    public List<Borrowed> findAllBorroweds() {
        return borrowedRepository.findAll();
    }



}
