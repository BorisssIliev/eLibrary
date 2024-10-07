package com.example.eLibrary.service.book;

import com.example.eLibrary.dto.savedBook.SavedBookRequestDto;
import com.example.eLibrary.dto.savedBook.SavedBookResponseDto;
import com.example.eLibrary.entity.book.SavedBook;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SavedBookService {

    SavedBookResponseDto saveBook(SavedBookRequestDto savedBookRequestDto);

    List<SavedBook> getSavedBooksForUserById(Long userId);

    List<SavedBook> getAllSavedBooksByUser();

    void deleteSavedBook(Long id);

    void deleteSavedBookForUser(Long savedBookId);
}
