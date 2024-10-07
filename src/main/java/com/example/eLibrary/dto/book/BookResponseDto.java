package com.example.eLibrary.dto.book;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BookResponseDto {

    private Long id;
    private String title;
    private String author;
    private String genre;
    private String isbn;
    private LocalDate publicationDate;

}
