package com.example.eLibrary.service.book.impl;


import com.example.eLibrary.converter.book.SaveBookConverter;
import com.example.eLibrary.dto.savedBook.SavedBookRequestDto;
import com.example.eLibrary.dto.savedBook.SavedBookResponseDto;
import com.example.eLibrary.entity.book.Book;
import com.example.eLibrary.entity.book.SavedBook;
import com.example.eLibrary.entity.user.User;
import com.example.eLibrary.exception.ResourceNotFoundException;
import com.example.eLibrary.exception.UnauthorizedException;
import com.example.eLibrary.repository.book.SavedBookRepository;
import com.example.eLibrary.security.utils.SecurityUtils;
import com.example.eLibrary.service.book.BookService;
import com.example.eLibrary.service.book.SavedBookService;
import com.example.eLibrary.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class SavedBookServiceImpl implements SavedBookService {

    @Autowired
    private SavedBookRepository savedBookRepository;

    @Autowired
    private SaveBookConverter saveBookConverter;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private SecurityUtils securityUtils;

    @Override
    public SavedBookResponseDto saveBook(SavedBookRequestDto savedBookRequestDto) {
        // 1. Извличане на потребителя от текущата сесия
        String userEmail = securityUtils.extractUserEmailFromToken();
        User user = userService.findUserByEmail(userEmail);
        if (user == null) {
            throw new RuntimeException("Потребителят не е намерен!");
        }

        // 2. Извличане на книгата
        Book book = bookService.getBookById(savedBookRequestDto.getBookId())
                .orElseThrow(() -> new RuntimeException("Книгата не е намерена!"));

        // 3. Проверка дали книгата вече е запазена
        if (isBookAlreadySaved(user.getId(), book.getId())) {
            throw new RuntimeException("Книгата вече е запазена!");
        }

        // 4. Създаване на обекта SavedBook
        SavedBook savedBook = new SavedBook();
        savedBook.setUser(user);
        savedBook.setBook(book);
        savedBook.setSavedDate(LocalDate.now());

        // 5. Запазване в базата
        SavedBook saved = savedBookRepository.save(savedBook);
        return saveBookConverter.toResponse(saved);
    }



    @Override
    public void deleteSavedBook(Long id) {
        SavedBook savedBook = savedBookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        savedBookRepository.delete(savedBook);
    }

    @Override
    public void deleteSavedBookForUser(Long savedBookId) {
        String userEmail = securityUtils.extractUserEmailFromToken();
        User user = userService.findUserByEmail(userEmail);

        if (user == null) {
            throw new RuntimeException("Потребителят не е намерен!");
        }

        SavedBook savedBook = savedBookRepository.findById(savedBookId)
                .orElseThrow(() -> new ResourceNotFoundException("Запазената книга не е намерена: " + savedBookId));

        if (!savedBook.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("Нямате права да изтриете тази книга!");
        }

        savedBookRepository.delete(savedBook);
    }

    @Override
    public List<SavedBook> getSavedBooksForUserById(Long userId) {

        return savedBookRepository.findByUserId(userId);

    }



    @Override
    public List<SavedBook> getAllSavedBooksByUser() {
        String email = securityUtils.extractUserEmailFromToken();
        if (email == null) {
            throw new RuntimeException("Грешка при извличане на потребителския email!");
        }

        User user = userService.findUserByEmail(email);
        if (user == null) {
            throw new RuntimeException("Потребителят не е намерен!");
        }

        return savedBookRepository.findByUserId(user.getId());
    }


    @Override
    public boolean isBookAlreadySaved(Long userId, Long bookId) {
        return savedBookRepository.findByUserId(userId)
                .stream()
                .anyMatch(savedBook -> savedBook.getBook().getId().equals(bookId));
    }

}
