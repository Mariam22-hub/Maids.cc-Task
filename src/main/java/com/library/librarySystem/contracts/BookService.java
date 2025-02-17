package com.library.librarySystem.contracts;

import com.library.librarySystem.dto.BookDto;
import com.library.librarySystem.model.Book;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface BookService {
    @Cacheable(value = "books")
    public List<BookDto> getAllBooks();

    @CachePut(value = "book", key = "#id")
    public BookDto updateBook(Long id, BookDto updatedBook);

    @CacheEvict(value = "books", allEntries = true)
    public BookDto createBook(Book book) throws Exception;

    @Cacheable(value = "book", key = "#id")
    public BookDto getBookById(Long id);

    @CacheEvict(value = {"books", "book"}, key = "#id")
    public void deleteBook(Long id);
}