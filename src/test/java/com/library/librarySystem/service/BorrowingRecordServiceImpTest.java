package com.library.librarySystem.service;

import com.library.librarySystem.exception.DuplicateEntryException;
import com.library.librarySystem.exception.ResourceNotFoundException;
import com.library.librarySystem.model.Book;
import com.library.librarySystem.model.BorrowingRecord;
import com.library.librarySystem.model.Patron;
import com.library.librarySystem.respository.BookRepository;
import com.library.librarySystem.respository.BorrowingRecordRepository;
import com.library.librarySystem.respository.PatronRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowingRecordServiceImpTest {

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    @InjectMocks
    private BorrowingRecordServiceImp borrowingRecordService;

    private Book book;
    private Patron patron;
    private BorrowingRecord borrowingRecord;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAvailable(true);

        patron = new Patron();
        patron.setId(1L);
        patron.setName("John Doe");

        borrowingRecord = new BorrowingRecord(book, patron, false);
    }

    @Test
    void testBorrowBook_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(borrowingRecordRepository.findByBookIdAndPatronIdAndReturnedFalse(1L, 1L))
                .thenReturn(Optional.empty());
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(borrowingRecord);

        String result = borrowingRecordService.borrowBook(1L, 1L);

        assertEquals("Book with ID 1 successfully borrowed by Patron ID 1", result);
        assertFalse(book.isAvailable());

        verify(borrowingRecordRepository, times(1)).save(any(BorrowingRecord.class));
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testBorrowBook_BookNotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> borrowingRecordService.borrowBook(1L, 1L));

        verify(borrowingRecordRepository, never()).save(any(BorrowingRecord.class));
    }

    @Test
    void testBorrowBook_PatronNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> borrowingRecordService.borrowBook(1L, 1L));

        verify(borrowingRecordRepository, never()).save(any(BorrowingRecord.class));
    }

    @Test
    void testBorrowBook_BookAlreadyBorrowed() {
        book.setAvailable(false);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));

        assertThrows(DuplicateEntryException.class, () -> borrowingRecordService.borrowBook(1L, 1L));

        verify(borrowingRecordRepository, never()).save(any(BorrowingRecord.class));
    }

    @Test
    void testBorrowBook_AlreadyBorrowedBySamePatron() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(borrowingRecordRepository.findByBookIdAndPatronIdAndReturnedFalse(1L, 1L))
                .thenReturn(Optional.of(borrowingRecord));

        assertThrows(IllegalStateException.class, () -> borrowingRecordService.borrowBook(1L, 1L));

        verify(borrowingRecordRepository, never()).save(any(BorrowingRecord.class));
    }

    @Test
    void testReturnBook_Success() {
        when(borrowingRecordRepository.findByBookIdAndPatronIdAndReturnedFalse(1L, 1L))
                .thenReturn(Optional.of(borrowingRecord));
        when(bookRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.saveAndFlush(any(Book.class))).thenReturn(book);

        String result = borrowingRecordService.returnBook(1L, 1L);

        assertEquals("Book with ID 1 successfully returned by Patron ID 1", result);
        assertTrue(borrowingRecord.isReturned());
        assertTrue(book.isAvailable());

        verify(borrowingRecordRepository, times(1)).save(any(BorrowingRecord.class));
        verify(bookRepository, times(1)).saveAndFlush(book);
    }

    @Test
    void testReturnBook_NoActiveBorrowRecord() {
        when(borrowingRecordRepository.findByBookIdAndPatronIdAndReturnedFalse(1L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> borrowingRecordService.returnBook(1L, 1L));

        verify(borrowingRecordRepository, never()).save(any(BorrowingRecord.class));
    }

    @Test
    void testReturnBook_BookDoesNotExist() {
        when(borrowingRecordRepository.findByBookIdAndPatronIdAndReturnedFalse(1L, 1L))
                .thenReturn(Optional.of(borrowingRecord));
        when(bookRepository.existsById(1L)).thenReturn(false);

        String result = borrowingRecordService.returnBook(1L, 1L);

        assertEquals("Book with ID 1 successfully returned by Patron ID 1", result);
        assertTrue(borrowingRecord.isReturned());

        verify(bookRepository, never()).saveAndFlush(any(Book.class));
    }
}