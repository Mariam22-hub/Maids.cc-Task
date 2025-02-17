package com.library.librarySystem.service;

import com.library.librarySystem.contracts.BookService;
import com.library.librarySystem.dto.BookDto;
import com.library.librarySystem.exception.DuplicateEntryException;
import com.library.librarySystem.exception.ResourceNotFoundException;
import com.library.librarySystem.model.Book;
import com.library.librarySystem.respository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class BookServiceImp implements BookService {

    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Cacheable(value = "books")
    public List<BookDto> getAllBooks() {
        return bookRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Cacheable(value = "book", key = "#id")
    public BookDto getBookById(Long id) {
        return modelMapper.map(bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id)), BookDto.class);
    }

    @Transactional
    @CacheEvict(value = "books", allEntries = true)
    public BookDto createBook(Book book) {
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new DuplicateEntryException("A book with ISBN " + book.getIsbn() + " already exists.");
        }
        return modelMapper.map(bookRepository.save(book), BookDto.class);
    }

    @Transactional
    @CachePut(value = "book", key = "#id")
    public BookDto updateBook(Long id, BookDto updateBook) {
        Book updatedBook = bookRepository.findById(id)
                .map(existingBook -> {
                    modelMapper.map(updateBook, existingBook);
                    return bookRepository.save(existingBook);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));

        return convertToDto(updatedBook);
    }

    @Transactional
    @CacheEvict(value = {"books", "book"}, key = "#bookId")
    public void deleteBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        if (book.isCurrentlyBorrowed()) {
            throw new IllegalStateException("Cannot delete book with active borrowing records.");
        }
        bookRepository.deleteById(bookId);
    }

    private BookDto convertToDto(Book book) {
        return modelMapper.map(book, BookDto.class);
    }

}