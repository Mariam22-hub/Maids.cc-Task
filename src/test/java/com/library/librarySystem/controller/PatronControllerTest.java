package com.library.librarySystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.librarySystem.contracts.PatronService;
import com.library.librarySystem.dto.PatronDto;
import com.library.librarySystem.model.Patron;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PatronControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PatronService patronService;

    @InjectMocks
    private PatronController patronController;

    private PatronDto patronDto;
    private Patron patron;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(patronController).build();

        patronDto = new PatronDto();
        patronDto.setName("John Doe");
        patronDto.setEmail("johndoe@example.com");
        patronDto.setPhone("+123456789");
        patronDto.setAddress("123 Library Street");

        patron = new Patron();
        patron.setId(1L);
        patron.setName("John Doe");
        patron.setEmail("johndoe@example.com");
        patron.setPhone("+123456789");
        patron.setAddress("123 Library Street");
    }

    @Test
    void testGetAllPatrons() throws Exception {
        List<PatronDto> patrons = Arrays.asList(patronDto);
        when(patronService.getAllPatrons()).thenReturn(patrons);

        mockMvc.perform(get("/api/patrons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"));

        verify(patronService, times(1)).getAllPatrons();
    }

    @Test
    void testGetPatronById() throws Exception {
        when(patronService.getPatronById(1L)).thenReturn(patronDto);

        mockMvc.perform(get("/api/patrons/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(patronService, times(1)).getPatronById(1L);
    }

    @Test
    void testCreatePatron() throws Exception {
        when(patronService.createPatron(any(Patron.class))).thenReturn(patronDto);

        mockMvc.perform(post("/api/patrons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patron)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(patronService, times(1)).createPatron(any(Patron.class));
    }

    @Test
    void testUpdatePatron() throws Exception {
        when(patronService.updatePatron(anyLong(), any(PatronDto.class))).thenReturn(patronDto);

        mockMvc.perform(put("/api/patrons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patronDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(patronService, times(1)).updatePatron(anyLong(), any(PatronDto.class));
    }

    @Test
    void testDeletePatron() throws Exception {
        doNothing().when(patronService).deletePatron(1L);

        mockMvc.perform(delete("/api/patrons/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Patron deleted successfully."));

        verify(patronService, times(1)).deletePatron(1L);
    }
}