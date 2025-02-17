package com.library.librarySystem.contracts;


import java.util.List;

public interface BorrowingRecordService {
    public String borrowBook(Long bookId, Long patronId);
    public String returnBook(Long bookId, Long patronId);
}