package com.library.librarySystem.service;

import com.library.librarySystem.dto.BookDto;
import com.library.librarySystem.exception.DuplicateEntryException;
import com.library.librarySystem.exception.ResourceNotFoundException;
import com.library.librarySystem.model.Book;
import com.library.librarySystem.respository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImpTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BookServiceImp bookService;

    private Book spyBook;

    private Book book;
    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setIsbn("9781234567897");
        book.setAuthor("robert");
        book.setGenre("classic");
        book.setAvailable(true);
        book.setPublicationYear(2000);
        book.setPageCount(20);

        bookDto = new BookDto();
        bookDto.setTitle("Test Book");
        bookDto.setAuthor("robert");
        bookDto.setPublicationYear(2000);
        bookDto.setGenre("classic");
        bookDto.setPageCount(20);

        spyBook = spy(book);
    }

    @Test
    void testGetAllBooks() {
        when(bookRepository.findAll()).thenReturn(Stream.of(book).collect(Collectors.toList()));
        when(modelMapper.map(any(Book.class), eq(BookDto.class))).thenReturn(bookDto);

        List<BookDto> books = bookService.getAllBooks();

        assertEquals(1, books.size());
        assertEquals("Test Book", books.get(0).getTitle());

        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testGetBookById_Found() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(modelMapper.map(any(Book.class), eq(BookDto.class))).thenReturn(bookDto);

        BookDto foundBook = bookService.getBookById(1L);

        assertNotNull(foundBook);
        assertEquals("Test Book", foundBook.getTitle());

        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void testGetBookById_NotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(1L));

        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateBook_Success() throws Exception {
        when(bookRepository.existsByIsbn(book.getIsbn())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(modelMapper.map(any(Book.class), eq(BookDto.class))).thenReturn(bookDto);

        BookDto savedBook = bookService.createBook(book);

        assertNotNull(savedBook);
        assertEquals("Test Book", savedBook.getTitle());

        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testCreateBook_DuplicateEntry() {
        when(bookRepository.existsByIsbn(book.getIsbn())).thenReturn(true);

        assertThrows(DuplicateEntryException.class, () -> bookService.createBook(book));

        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void testUpdateBook_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        doAnswer(invocation -> null).when(modelMapper).map(any(BookDto.class), any(Book.class));

        when(modelMapper.map(any(Book.class), eq(BookDto.class))).thenReturn(bookDto);

        BookDto updatedBook = bookService.updateBook(1L, bookDto);

        assertNotNull(updatedBook);
        assertEquals("Test Book", updatedBook.getTitle());

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testUpdateBook_NotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.updateBook(1L, bookDto));

        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void testDeleteBook_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        lenient().when(spyBook.isCurrentlyBorrowed()).thenReturn(false);
        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteBook_NotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.deleteBook(1L));

        verify(bookRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteBook_AlreadyBorrowed() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(spyBook));
        when(spyBook.isCurrentlyBorrowed()).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> bookService.deleteBook(1L));

        verify(bookRepository, never()).deleteById(anyLong());
    }
}