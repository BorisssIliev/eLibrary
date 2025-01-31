package com.example.eLibrary.controller.frontEnd;

import com.example.eLibrary.dto.savedBook.SavedBookRequestDto;
import com.example.eLibrary.entity.user.User;
import com.example.eLibrary.service.book.BookService;
import com.example.eLibrary.service.book.SavedBookService;
import com.example.eLibrary.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/basket")
public class BasketController {

    @Autowired
    private SavedBookService savedBookService;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @PostMapping("/add/{bookId}")
    public ResponseEntity<String> addBookToBasket(@PathVariable Long bookId) {
        // Проверка за логнат потребител
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You must be logged in to add books to the basket.");
        }

        // Вземаме текущия потребител
        String userEmail = authentication.getName();
        User user = userService.findUserByEmail(userEmail);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("User not found. Please log in again.");
        }

        // Проверка дали книгата вече е в кошницата
        if (savedBookService.isBookAlreadySaved(user.getId(), bookId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("This book is already in your basket.");
        }

        // Запазваме книгата в "basket"
        SavedBookRequestDto requestDto = SavedBookRequestDto.builder()
                .bookId(bookId)
                .build();
        savedBookService.saveBook(requestDto);

        return ResponseEntity.ok("Book added to basket successfully.");
    }
}
