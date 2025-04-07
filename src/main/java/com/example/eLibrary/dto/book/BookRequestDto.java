package com.example.eLibrary.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BookRequestDto {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @NotBlank(message = "Genre is required")
    private String genre;

    @NotBlank(message = "ISBN is required")
    private String isbn;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Publication date is required")
    private LocalDate publicationDate;

    @NotBlank(message = "Summary is required")
    private String summary;
}
