package com.example.biblio.repository;

import com.example.biblio.model.Borrowed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowedRepository extends JpaRepository<Borrowed, Long> {

}