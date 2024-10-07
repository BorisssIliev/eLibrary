package com.example.eLibrary.converter.book;

import com.example.eLibrary.dto.savedBook.SavedBookRequestDto;
import com.example.eLibrary.dto.savedBook.SavedBookResponseDto;
import com.example.eLibrary.entity.book.Book;
import com.example.eLibrary.entity.book.SavedBook;
import com.example.eLibrary.entity.user.User;
import com.example.eLibrary.repository.book.SavedBookRepository;
import com.example.eLibrary.security.utils.SecurityUtils;
import com.example.eLibrary.service.book.BookService;
import com.example.eLibrary.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class SaveBookConverter {

    private final BookService bookService;

    private final SavedBookRepository savedBookRepository;

    private final UserService userService;

    private final SecurityUtils securityUtils;

    public SavedBook toSavedBook (SavedBookRequestDto savedBookRequestDto){
        Book foundBook = bookService.getBookById(savedBookRequestDto.getBookId()).orElseThrow();
        String userEmail = securityUtils.extractUserEmailFromToken();
        User user = userService.findUserByEmail(userEmail);

        return SavedBook.builder()
                .book(foundBook)
                .user(user)
                .savedDate(LocalDate.now())
                .build();

    }

    public SavedBookResponseDto toResponse(SavedBook savedBook){
        return new SavedBookResponseDto(
                savedBook.getId(),
                savedBook.getBook(),
                savedBook.getUser(),
                savedBook.getSavedDate());
    }


}
