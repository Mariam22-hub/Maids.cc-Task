package com.library.librarySystem.respository;

import com.library.librarySystem.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookRepositoryTest {

    @Mock
    private BookRepository bookRepository;

    private List<Book> inMemoryBookDB;

    @BeforeEach
    void setUp() {
        inMemoryBookDB = new ArrayList<>();

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Effective Java");
        book.setIsbn("9780134685991");

        inMemoryBookDB.add(book);
    }

    @Test
    void testSaveBook() {
        Book newBook = new Book();
        newBook.setId(2L);
        newBook.setTitle("Clean Code");
        newBook.setIsbn("9780132350884");

        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
            Book savedBook = invocation.getArgument(0);
            inMemoryBookDB.add(savedBook);
            return savedBook;
        });

        Book savedBook = bookRepository.save(newBook);
        assertTrue(inMemoryBookDB.contains(savedBook));
        verify(bookRepository, times(1)).save(newBook);
    }

    @Test
    void testFindById() {
        when(bookRepository.findById(1L)).thenReturn(inMemoryBookDB.stream()
                .filter(book -> book.getId().equals(1L))
                .findFirst());

        Optional<Book> foundBook = bookRepository.findById(1L);
        assertTrue(foundBook.isPresent());
        assertEquals("Effective Java", foundBook.get().getTitle());
    }

    @Test
    void testExistsByIsbn() {
        when(bookRepository.existsByIsbn("9780134685991")).thenReturn(true);

        assertTrue(bookRepository.existsByIsbn("9780134685991"));
        assertFalse(bookRepository.existsByIsbn("0000000000000"));
    }
}