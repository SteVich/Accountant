package com.example.biblio.controller;

import com.example.biblio.dto.BorrowedDTO;
import com.example.biblio.payload.ApiResponse;
import com.example.biblio.service.BorrowedService;
import com.example.biblio.transfer.BorrowedMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/books/borroweds")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BorrowedController {

    BorrowedService borrowedService;
    BorrowedMapper borrowedMapper;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<BorrowedDTO>> findAllBorroweds() {
        return ResponseEntity.ok(borrowedMapper.toBorrowedDTOs(borrowedService.findAllBorroweds()));
    }

    @PreAuthorize("hasPermission(#id, 'Borrowed', 'READ')")
    @GetMapping("/{id}")
    public ResponseEntity<BorrowedDTO> findBorrowedById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(borrowedMapper.toBorrowedDTO(borrowedService.findBorrowedById(id)));
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<ApiResponse> createBorrowed(@PathVariable Long bookId) {
        borrowedService.createBorrowed(bookId);

        return ResponseEntity.ok().body(new ApiResponse(true, "You successfully borrowed a book"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteBorrowed(@PathVariable("id") Long id) {
        borrowedService.deleteBorrowed(id);

        return ResponseEntity.noContent().build();
    }

}