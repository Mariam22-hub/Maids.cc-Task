package com.library.librarySystem.respository;

import com.library.librarySystem.model.Patron;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatronRepositoryTest {

    @Mock
    private PatronRepository patronRepository;

    private List<Patron> inMemoryPatronDB;

    @BeforeEach
    void setUp() {
        inMemoryPatronDB = new ArrayList<>();

        Patron patron = new Patron();
        patron.setId(1L);
        patron.setName("John Doe");
        patron.setEmail("john.doe@example.com");

        inMemoryPatronDB.add(patron);
    }

    @Test
    void testExistsByEmail() {
        when(patronRepository.existsByEmail("john.doe@example.com")).thenReturn(true);

        assertTrue(patronRepository.existsByEmail("john.doe@example.com"));
        assertFalse(patronRepository.existsByEmail("invalid@example.com"));
    }
}