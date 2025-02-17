package com.library.librarySystem.controller;

import com.library.librarySystem.contracts.PatronService;
import com.library.librarySystem.dto.PatronDto;
import com.library.librarySystem.model.Patron;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patrons")
@RequiredArgsConstructor
@Tag(name = "Patrons", description = "Endpoints for managing library patrons")
public class PatronController {

    private final PatronService patronService;

    @Operation(summary = "Get all patrons", description = "Retrieves a list of all patrons registered in the library.")
    @GetMapping
    public List<PatronDto> getAllPatrons() {
        return patronService.getAllPatrons();
    }

    @Operation(summary = "Get a patron by ID", description = "Retrieves details of a specific patron by their ID.")
    @GetMapping("/{id}")
    public ResponseEntity<PatronDto> getPatronById(
            @Parameter(description = "ID of the patron to be retrieved", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(patronService.getPatronById(id));
    }

    @Operation(summary = "Create a new patron", description = "Registers a new patron in the library system.")
    @PostMapping
    public ResponseEntity<PatronDto> createPatron(
            @Parameter(description = "Patron object containing required details", required = true)
            @Valid @RequestBody Patron patron) throws Exception {
        return ResponseEntity.ok(patronService.createPatron(patron));
    }

    @Operation(summary = "Update a patron", description = "Updates details of an existing patron.")
    @PutMapping("/{id}")
    public ResponseEntity<PatronDto> updatePatron(
            @Parameter(description = "ID of the patron to be updated", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated patron details", required = true)
            @Valid @RequestBody PatronDto patronDto) {
        return ResponseEntity.ok(patronService.updatePatron(id, patronDto));
    }

    @Operation(summary = "Delete a patron", description = "Removes a patron from the library system by their ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatron(
            @Parameter(description = "ID of the patron to be deleted", required = true)
            @PathVariable Long id) {
        patronService.deletePatron(id);
        return ResponseEntity.ok("Patron deleted successfully.");
    }
}