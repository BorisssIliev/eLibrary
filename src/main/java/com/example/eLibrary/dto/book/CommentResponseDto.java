package com.example.eLibrary.dto.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CommentResponseDto {

    private Long id;
    private String authorName;
    private String content;
    private LocalDateTime createdAt;
    private boolean isAuthor;

}