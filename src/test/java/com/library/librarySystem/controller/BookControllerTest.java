package com.library.librarySystem.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.librarySystem.contracts.BookService;
import com.library.librarySystem.dto.BookDto;
import com.library.librarySystem.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private BookDto bookDto;
    private Book book;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();

        bookDto = new BookDto();
        bookDto.setTitle("Test Book");
        bookDto.setPageCount(20);
        bookDto.setAuthor("Test Author");
        bookDto.setGenre("Fiction");
        bookDto.setPrice(BigDecimal.valueOf(19.99));

        book = new Book();
        book.setTitle("Test Book");
        book.setPageCount(20);
        book.setIsbn("9780132350888");
        book.setAuthor("Test Author");
        book.setGenre("Fiction");
        book.setDescription("a book about fiction");
        book.setPrice(BigDecimal.valueOf(19.99));
        book.setPublicationYear(1900);
    }

    @Test
    void testGetAllBooks() throws Exception {
        List<BookDto> books = Arrays.asList(bookDto);
        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Book"));

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void testGetBookById() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(bookDto);

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Book"));

        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    void testCreateBook() throws Exception {
        when(bookService.createBook(any(Book.class))).thenReturn(bookDto);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Book"));

        verify(bookService, times(1)).createBook(any(Book.class));
    }

    @Test
    void testUpdateBook() throws Exception {
        when(bookService.updateBook(anyLong(), any(BookDto.class))).thenReturn(bookDto);

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Book"));

        verify(bookService, times(1)).updateBook(anyLong(), any(BookDto.class));
    }

    @Test
    void testDeleteBook() throws Exception {
        doNothing().when(bookService).deleteBook(1L);

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Book deleted successfully."));

        verify(bookService, times(1)).deleteBook(1L);
    }
}