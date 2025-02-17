package com.library.librarySystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Book extends Auditable implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Author is required")
    @Size(max = 100, message = "Author name cannot exceed 100 characters")
    @Column(nullable = false)
    private String author;

    @Min(value = 1500, message = "Publication year must be after 1500")
    @Max(value = 2100, message = "Publication year must be realistic")
    @Column(name = "publication_year")
    private int publicationYear;

    @NotBlank(message = "ISBN is required")
    @Pattern(regexp = "^(97(8|9))?\\d{9}(\\d|X)$", message = "Invalid ISBN format")
    @Column(unique = true, nullable = false)
    private String isbn;

    @NotBlank(message = "Genre is required")
    @Size(max = 50, message = "Genre cannot exceed 50 characters")
    @Column(nullable = false)
    private String genre;

    @DecimalMin(value = "0.00", message = "Price cannot be negative")
    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private boolean available;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Lob
    private String description;

    @Min(value = 1, message = "Page count must be at least 1")
    @Column(name = "page_count", nullable = false)
    private int pageCount;

    @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ToString.Exclude
    private List<BorrowingRecord> borrowingRecords;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = true, insertable = false)
    private LocalDateTime lastModifiedAt;

    public boolean isCurrentlyBorrowed() {
        if (borrowingRecords == null) {
            return false;
        }
        return borrowingRecords.stream().anyMatch(record -> !record.isReturned());
    }

}