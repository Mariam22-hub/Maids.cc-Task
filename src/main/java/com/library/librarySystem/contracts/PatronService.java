package com.library.librarySystem.contracts;

import com.library.librarySystem.dto.BookDto;
import com.library.librarySystem.dto.PatronDto;
import com.library.librarySystem.model.Book;
import com.library.librarySystem.model.Patron;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface PatronService {
    @CacheEvict(value = {"patrons", "patron"}, key = "#id")
    public void deletePatron(Long id);

    @CachePut(value = "patron", key = "#id")
    public PatronDto updatePatron(Long id, PatronDto updatedPatronDto);

    @CacheEvict(value = "patrons", allEntries = true)
    public PatronDto createPatron(Patron patron) throws Exception;

    @Cacheable(value = "patron", key = "#id")
    public PatronDto getPatronById(Long id);

    @Cacheable(value = "patrons")
    public List<PatronDto> getAllPatrons();
}