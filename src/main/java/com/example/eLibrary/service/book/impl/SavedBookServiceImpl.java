package com.example.eLibrary.service.book.impl;


import com.example.eLibrary.converter.book.SaveBookConverter;
import com.example.eLibrary.dto.savedBook.SavedBookRequestDto;
import com.example.eLibrary.dto.savedBook.SavedBookResponseDto;
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
        SavedBook savedBook = saveBookConverter.toSavedBook(savedBookRequestDto);
        System.out.println("Saving book: " + savedBook.getBook().getId() + " for user: " + savedBook.getUser().getEmail());
        SavedBook saveSavedBook = savedBookRepository.save(savedBook);
        System.out.println("Saved book with ID: " + saveSavedBook.getId());
        return saveBookConverter.toResponse(saveSavedBook);
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
        if (user == null){
            System.out.println("User not found");
        }

        SavedBook savedBook = savedBookRepository.findById(savedBookId)
                .orElseThrow(() -> new ResourceNotFoundException("SavedBook not found with id: " + savedBookId));

        if (!savedBook.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You do not have permission to delete this book.");
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
        if (email != null) {
            User user = userService.findUserByEmail(email);
            if (user != null) {
                return savedBookRepository.findByUserId(user.getId());
            }
        }

        return Collections.emptyList();
    }
}
