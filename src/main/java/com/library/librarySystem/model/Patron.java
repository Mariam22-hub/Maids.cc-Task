package com.library.librarySystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "patrons")
@Data
@NoArgsConstructor
@ToString
public class Patron extends Auditable implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,15}$", message = "Invalid phone number format")
    @Column(nullable = false)
    private String phone;

    @Size(max = 255, message = "Address must be at most 255 characters")
    private String address;

    @OneToMany(mappedBy = "patron", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ToString.Exclude
    private List<BorrowingRecord> borrowingRecords;

    public boolean isCurrentlyBorrowed() {
        if (borrowingRecords == null) {
            return false;
        }
        return borrowingRecords.stream().anyMatch(record -> !record.isReturned());
    }

}