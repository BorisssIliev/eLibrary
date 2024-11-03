package com.example.eLibrary.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BookRequestDto {


    @NotBlank(message = "Title is required")
    @Size(max = 30, message = "Title must be at most 30 characters")
    private String title;

    @NotBlank(message = "Author is required")
    @Size(max = 30, message = "Author must be at most 30 characters")
    private String author;

    @NotBlank(message = "Genre is required")
    @Size(max = 30, message = "Genre must be at most 30 characters")
    private String genre;

    @NotBlank(message = "Isbn is required")
    @Size(max = 30, message = "Isbn must be at most 30 characters")
    private String isbn;

    @NotNull(message = "publication date is required")
    private LocalDate publicationDate;

    @NotBlank(message = "summary is required")
    private String summary;


}
