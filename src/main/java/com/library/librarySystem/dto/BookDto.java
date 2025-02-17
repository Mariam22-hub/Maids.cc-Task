package com.library.librarySystem.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class BookDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private int pageCount;
    private String author;
    private int publicationYear;
    private String genre;
    private BigDecimal price;
}