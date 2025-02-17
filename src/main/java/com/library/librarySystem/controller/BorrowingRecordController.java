package com.library.librarySystem.controller;

import com.library.librarySystem.contracts.BorrowingRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Borrowing Records", description = "Endpoints for borrowing and returning books")
public class BorrowingRecordController {

    private final BorrowingRecordService borrowingService;

    @Operation(
            summary = "Borrow a book",
            description = "Allows a patron to borrow a book from the library if it is available."
    )
    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<String> borrowBook(
            @Parameter(description = "ID of the book to be borrowed", required = true)
            @PathVariable Long bookId,
            @Parameter(description = "ID of the patron borrowing the book", required = true)
            @PathVariable Long patronId) {
        return ResponseEntity.ok(borrowingService.borrowBook(bookId, patronId));
    }

    @Operation(
            summary = "Return a book",
            description = "Marks a book as returned by a patron, making it available for borrowing again."
    )
    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<String> returnBook(
            @Parameter(description = "ID of the book being returned", required = true)
            @PathVariable Long bookId,
            @Parameter(description = "ID of the patron returning the book", required = true)
            @PathVariable Long patronId) {
        return ResponseEntity.ok(borrowingService.returnBook(bookId, patronId));
    }
}