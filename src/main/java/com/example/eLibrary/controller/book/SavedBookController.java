package com.example.eLibrary.controller.book;

import com.example.eLibrary.converter.book.SaveBookConverter;
import com.example.eLibrary.dto.savedBook.SavedBookRequestDto;
import com.example.eLibrary.dto.savedBook.SavedBookResponseDto;
import com.example.eLibrary.entity.book.SavedBook;
import com.example.eLibrary.service.book.SavedBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class SavedBookController {

    @Autowired
    private SavedBookService savedBookService;

    @Autowired
    private SaveBookConverter saveBookConverter;

    @PostMapping("/user/saved-book")
    public ResponseEntity<SavedBookResponseDto> saveBook(@RequestBody SavedBookRequestDto savedBookRequestDto) {
        System.out.println("Received request to save book with ID: " + savedBookRequestDto.getBookId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBookService.saveBook(savedBookRequestDto));
    }

    @GetMapping("/admin/saved-book/get-by-user-id/{id}")
    public ResponseEntity<List<SavedBook>> getBySavedBooksByUserId(@PathVariable Long id){
        List<SavedBook> foundSaveBooks = savedBookService.getSavedBooksForUserById(id);
        return ResponseEntity.ok(foundSaveBooks);

    }

    @GetMapping("/user/saved-book/get-by-user")
    public ResponseEntity<List<SavedBook>> getByUserToken(){
        List<SavedBook> savedBooks = savedBookService.getAllSavedBooksByUser();

        if (savedBooks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(savedBooks);

    }

    @DeleteMapping("/user/saved-book/delete-by-user/{savedBookId}")
    public ResponseEntity<Void> deleteSavedBook(@PathVariable Long savedBookId) {
        savedBookService.deleteSavedBookForUser(savedBookId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/admin/saved-book/delete-by-id/{id}")
    public ResponseEntity<Void> deleteSavedBookById(@PathVariable Long id){
        savedBookService.deleteSavedBook(id);
        return ResponseEntity.noContent().build();
    }
}
