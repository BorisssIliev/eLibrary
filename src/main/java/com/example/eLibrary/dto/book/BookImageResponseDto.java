package com.example.eLibrary.dto.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookImageResponseDto {

    private Long id;
    private String imageName;
    private Long bookId;
}

