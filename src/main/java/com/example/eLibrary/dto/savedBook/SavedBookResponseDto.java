package com.example.eLibrary.dto.savedBook;

import com.example.eLibrary.entity.book.Book;
import com.example.eLibrary.entity.user.User;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Data
public class SavedBookResponseDto {

    private Long id;
    private Book book;
    private User user;
    private LocalDate savedDate;

}
