package com.library.librarySystem.respository;

import com.library.librarySystem.model.Book;
import com.library.librarySystem.model.Patron;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatronRepository extends JpaRepository<Patron, Long> {
    Boolean existsByEmail(String email);
}