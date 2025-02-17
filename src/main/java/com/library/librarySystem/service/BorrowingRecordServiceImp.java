package com.library.librarySystem.service;

import com.library.librarySystem.contracts.BorrowingRecordService;
import com.library.librarySystem.exception.DuplicateEntryException;
import com.library.librarySystem.exception.ResourceNotFoundException;
import com.library.librarySystem.model.Book;
import com.library.librarySystem.model.BorrowingRecord;
import com.library.librarySystem.model.Patron;
import com.library.librarySystem.respository.BookRepository;
import com.library.librarySystem.respository.BorrowingRecordRepository;
import com.library.librarySystem.respository.PatronRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class BorrowingRecordServiceImp implements BorrowingRecordService {
    private final BorrowingRecordRepository borrowingRecordRepository;
    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;

    @Transactional(rollbackOn = DuplicateEntryException.class)
    public String borrowBook(Long bookId, Long patronId){
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        Patron patron = patronRepository.findById(patronId).orElseThrow(() -> new ResourceNotFoundException("Patron not found"));

        if(!book.isAvailable()){
            throw new DuplicateEntryException("Book with id: " + bookId + " is already borrowed");
        }

        Optional<BorrowingRecord> borrowingRecord = borrowingRecordRepository.findByBookIdAndPatronIdAndReturnedFalse(bookId, patronId);
        if (borrowingRecord.isPresent()) {
            throw new IllegalStateException("Patron with id: " + patronId + " already has borrowed this book");
        }

        BorrowingRecord record = new BorrowingRecord(book, patron, false);

        borrowingRecordRepository.save(record);
        book.setAvailable(false);
        bookRepository.save(book);

        return "Book with ID " + bookId + " successfully borrowed by Patron ID " + patronId;
    }

    @Transactional
    public String returnBook(Long bookId, Long patronId) {
        BorrowingRecord record = borrowingRecordRepository.findByBookIdAndPatronIdAndReturnedFalse(bookId, patronId)
                .orElseThrow(() -> new ResourceNotFoundException("No active borrowing record found for this book and patron."));

        record.setReturned(true);
        borrowingRecordRepository.save(record);

        if (bookRepository.existsById(bookId)) {
            Book book = record.getBook();
            book.setAvailable(true);
            bookRepository.saveAndFlush(book);
        }

        return "Book with ID " + bookId + " successfully returned by Patron ID " + patronId;
    }

}