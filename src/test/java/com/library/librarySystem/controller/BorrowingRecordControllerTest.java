package com.library.librarySystem.controller;

import static org.junit.jupiter.api.Assertions.*;
import com.library.librarySystem.contracts.BorrowingRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BorrowingRecordControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BorrowingRecordService borrowingService;

    @InjectMocks
    private BorrowingRecordController borrowingRecordController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(borrowingRecordController).build();
    }

    @Test
    void testBorrowBook() throws Exception {
        Long bookId = 1L;
        Long patronId = 2L;
        String successMessage = "Book with ID 1 successfully borrowed by Patron ID 2";

        when(borrowingService.borrowBook(anyLong(), anyLong())).thenReturn(successMessage);

        mockMvc.perform(post("/api/borrow/1/patron/2"))
                .andExpect(status().isOk())
                .andExpect(content().string(successMessage));

        verify(borrowingService, times(1)).borrowBook(bookId, patronId);
    }

    @Test
    void testReturnBook() throws Exception {
        Long bookId = 1L;
        Long patronId = 2L;
        String successMessage = "Book with ID 1 successfully returned by Patron ID 2";

        when(borrowingService.returnBook(anyLong(), anyLong())).thenReturn(successMessage);

        mockMvc.perform(put("/api/return/1/patron/2"))
                .andExpect(status().isOk())
                .andExpect(content().string(successMessage));

        verify(borrowingService, times(1)).returnBook(bookId, patronId);
    }
}