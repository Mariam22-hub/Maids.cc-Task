package com.library.librarySystem.respository;

import com.library.librarySystem.model.Book;
import com.library.librarySystem.model.BorrowingRecord;
import com.library.librarySystem.model.Patron;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowingRecordRepositoryTest {

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    private List<BorrowingRecord> inMemoryRecords;

    @BeforeEach
    void setUp() {
        inMemoryRecords = new ArrayList<>();

        Book book = new Book();
        book.setId(1L);
        Patron patron = new Patron();
        patron.setId(1L);

        BorrowingRecord record = new BorrowingRecord(book, patron, false);
        inMemoryRecords.add(record);
    }

    @Test
    void testFindByBookIdAndPatronIdAndReturnedFalse() {
        when(borrowingRecordRepository.findByBookIdAndPatronIdAndReturnedFalse(1L, 1L))
                .thenReturn(inMemoryRecords.stream()
                        .filter(record -> record.getBook().getId().equals(1L)
                                && record.getPatron().getId().equals(1L)
                                && !record.isReturned())
                        .findFirst());

        Optional<BorrowingRecord> foundRecord = borrowingRecordRepository.findByBookIdAndPatronIdAndReturnedFalse(1L, 1L);

        assertTrue(foundRecord.isPresent());
        assertFalse(foundRecord.get().isReturned());
    }
}