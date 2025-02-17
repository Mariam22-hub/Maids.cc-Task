package com.library.librarySystem.controller;

import com.library.librarySystem.contracts.BookService;
import com.library.librarySystem.dto.BookDto;
import com.library.librarySystem.model.Book;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "Endpoints for managing books in the library system")
public class BookController {

    private final BookService bookService;

    @Operation(summary = "Get all books", description = "Retrieves a list of all books available in the library.")
    @GetMapping
    public List<BookDto> getAllBooks() {
        return bookService.getAllBooks();
    }

    @Operation(summary = "Get a book by ID", description = "Retrieves details of a specific book by its ID.")
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(
            @Parameter(description = "ID of the book to be retrieved", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @Operation(summary = "Create a new book", description = "Adds a new book to the library system.")
    @PostMapping
    public ResponseEntity<BookDto> createBook(
            @Parameter(description = "Book object that needs to be created", required = true)
            @Valid @RequestBody Book book) throws Exception {
        return ResponseEntity.ok(bookService.createBook(book));
    }

    @Operation(summary = "Update a book", description = "Updates the details of an existing book.")
    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(
            @Parameter(description = "ID of the book to be updated", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated book details", required = true)
            @Valid @RequestBody BookDto bookDto) {
        return ResponseEntity.ok(bookService.updateBook(id, bookDto));
    }

    @Operation(summary = "Delete a book", description = "Removes a book from the library system by ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(
            @Parameter(description = "ID of the book to be deleted", required = true)
            @PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok("Book deleted successfully.");
    }
}