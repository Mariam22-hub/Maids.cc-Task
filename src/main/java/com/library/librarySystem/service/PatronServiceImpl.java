package com.library.librarySystem.service;

import com.library.librarySystem.contracts.PatronService;
import com.library.librarySystem.dto.PatronDto;
import com.library.librarySystem.exception.DuplicateEntryException;
import com.library.librarySystem.exception.ResourceNotFoundException;
import com.library.librarySystem.model.Patron;
import com.library.librarySystem.respository.PatronRepository;
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
public class PatronServiceImpl implements PatronService {

    private final PatronRepository patronRepository;
    private final ModelMapper modelMapper;

    @Cacheable(value = "patrons")
    public List<PatronDto> getAllPatrons() {
        return patronRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "patron", key = "#id")
    public PatronDto getPatronById(Long id) {
        Patron patron = patronRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patron not found with ID: " + id));
        return convertToDto(patron);
    }

    @Transactional
    @CacheEvict(value = "patrons", allEntries = true)
    public PatronDto createPatron(Patron patron) throws Exception {
        if (patronRepository.existsByEmail(patron.getEmail())) {
            throw new DuplicateEntryException("A patron with email " + patron.getEmail() + " already exists.");
        }
        return modelMapper.map(patronRepository.save(patron), PatronDto.class);
    }

    @Transactional
    @CachePut(value = "patron", key = "#id")
    public PatronDto updatePatron(Long id, PatronDto updatedPatronDto) {
        Patron updatedPatron = patronRepository.findById(id)
                .map(existingPatron -> {
                    modelMapper.map(updatedPatronDto, existingPatron);
                    return patronRepository.save(existingPatron);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Patron not found with ID: " + id));

        return convertToDto(updatedPatron);
    }

    @Transactional
    @CacheEvict(value = {"patrons", "patron"}, key = "#id")
    public void deletePatron(Long id) {
        Patron patron = patronRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Patron not found with ID: " + id));
        if (patron.isCurrentlyBorrowed()) {
            throw new IllegalStateException("Cannot delete patron with active borrowing records.");
        }
        patronRepository.deleteById(id);
    }


    private PatronDto convertToDto(Patron patron) {
        return modelMapper.map(patron, PatronDto.class);
    }

}