package com.library.librarySystem.service;

import com.library.librarySystem.dto.PatronDto;
import com.library.librarySystem.exception.DuplicateEntryException;
import com.library.librarySystem.exception.ResourceNotFoundException;
import com.library.librarySystem.model.Patron;
import com.library.librarySystem.respository.PatronRepository;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatronServiceImplTest {

    @Mock
    private PatronRepository patronRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PatronServiceImpl patronService;

    private Patron spyPatron;
    private Patron patron;
    private PatronDto patronDto;

    @BeforeEach
    void setUp() {
        patron = new Patron();
        patron.setId(1L);
        patron.setName("John Doe");
        patron.setEmail("john.doe@example.com");
        patron.setPhone("+123456789");
        patron.setAddress("123 Library Street");

        patronDto = new PatronDto();
        patronDto.setName("John Doe");
        patronDto.setEmail("john.doe@example.com");
        patronDto.setPhone("+123456789");
        patronDto.setAddress("123 Library Street");

        spyPatron = spy(patron);
    }

    @Test
    void testGetAllPatrons() {
        when(patronRepository.findAll()).thenReturn(Stream.of(patron).collect(Collectors.toList()));
        when(modelMapper.map(any(Patron.class), eq(PatronDto.class))).thenReturn(patronDto);

        List<PatronDto> patrons = patronService.getAllPatrons();

        assertEquals(1, patrons.size());
        assertEquals("John Doe", patrons.get(0).getName());

        verify(patronRepository, times(1)).findAll();
    }

    @Test
    void testGetPatronById_Found() {
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(modelMapper.map(any(Patron.class), eq(PatronDto.class))).thenReturn(patronDto);

        PatronDto foundPatron = patronService.getPatronById(1L);

        assertNotNull(foundPatron);
        assertEquals("John Doe", foundPatron.getName());

        verify(patronRepository, times(1)).findById(1L);
    }

    @Test
    void testGetPatronById_NotFound() {
        when(patronRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> patronService.getPatronById(1L));

        verify(patronRepository, times(1)).findById(1L);
    }

    @Test
    void testCreatePatron_Success() throws Exception {
        when(patronRepository.existsByEmail(patron.getEmail())).thenReturn(false);
        when(patronRepository.save(any(Patron.class))).thenReturn(patron);
        when(modelMapper.map(any(Patron.class), eq(PatronDto.class))).thenReturn(patronDto);

        PatronDto savedPatron = patronService.createPatron(patron);

        assertNotNull(savedPatron);
        assertEquals("John Doe", savedPatron.getName());

        verify(patronRepository, times(1)).save(patron);
    }

    @Test
    void testCreatePatron_DuplicateEntry() {
        when(patronRepository.existsByEmail(patron.getEmail())).thenReturn(true);

        assertThrows(DuplicateEntryException.class, () -> patronService.createPatron(patron));

        verify(patronRepository, never()).save(any(Patron.class));
    }

    @Test
    void testUpdatePatron_Success() {
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(patronRepository.save(any(Patron.class))).thenReturn(patron);

        doAnswer(invocation -> null).when(modelMapper).map(any(PatronDto.class), any(Patron.class));

        when(modelMapper.map(any(Patron.class), eq(PatronDto.class))).thenReturn(patronDto);

        PatronDto updatedPatron = patronService.updatePatron(1L, patronDto);

        assertNotNull(updatedPatron);
        assertEquals("John Doe", updatedPatron.getName());

        verify(patronRepository, times(1)).findById(1L);
        verify(patronRepository, times(1)).save(any(Patron.class));
    }

    @Test
    void testUpdatePatron_NotFound() {
        when(patronRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> patronService.updatePatron(1L, patronDto));

        verify(patronRepository, never()).save(any(Patron.class));
    }

    @Test
    void testDeletePatron_Success() {
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        lenient().when(spyPatron.isCurrentlyBorrowed()).thenReturn(false);

        patronService.deletePatron(1L);

        verify(patronRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeletePatron_NotFound() {
        when(patronRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> patronService.deletePatron(1L));

        verify(patronRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeletePatron_AlreadyBorrowed() {
        when(patronRepository.findById(1L)).thenReturn(Optional.of(spyPatron));
        doReturn(true).when(spyPatron).isCurrentlyBorrowed();

        assertThrows(IllegalStateException.class, () -> patronService.deletePatron(1L));

        verify(patronRepository, never()).deleteById(anyLong());
    }
}