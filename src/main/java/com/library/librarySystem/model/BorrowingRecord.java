package com.library.librarySystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "borrowing_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BorrowingRecord extends Auditable implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    @NotNull(message = "Book must be selected")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "patron_id", nullable = false)
    @NotNull(message = "Patron must be selected")
    private Patron patron;

    @NotNull(message = "Borrow date is required")
    @PastOrPresent(message = "Borrow date cannot be in the future")
    @Column(nullable = false)
    private LocalDate borrowDate;

    @FutureOrPresent(message = "Return date cannot be in the past")
    private LocalDate returnDate;

    @Column(nullable = false)
    private boolean returned;

    public BorrowingRecord(Book book, Patron patron, boolean returned) {
        this.book = book;
        this.returned = returned;
        this.patron = patron;
        this.borrowDate = LocalDate.now();
    }
}