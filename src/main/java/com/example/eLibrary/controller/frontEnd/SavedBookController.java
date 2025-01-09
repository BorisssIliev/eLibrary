//package com.example.eLibrary.controller.frontEnd;
//
//import com.example.eLibrary.dto.savedBook.SavedBookRequestDto;
//import com.example.eLibrary.dto.savedBook.SavedBookResponseDto;
//import com.example.eLibrary.entity.book.SavedBook;
//import com.example.eLibrary.entity.user.User;
//import com.example.eLibrary.service.book.SavedBookService;
//import com.example.eLibrary.service.user.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/v1/saved-books")
//@RequiredArgsConstructor
//public class SavedBookController {
//
//    private final SavedBookService savedBookService;
//    private final UserService userService;
//
//    @PostMapping("/add")
//    public ResponseEntity<String> saveBook(@RequestBody SavedBookRequestDto savedBookRequestDto) {
//        String userEmail = userService.getAuthUserEmail();
//        User user = userService.findUserByEmail(userEmail);
//
//        if (savedBookService.isBookAlreadySaved(user.getId(), savedBookRequestDto.getBookId())) {
//            return ResponseEntity.badRequest().body("Book is already saved.");
//        }
//
//        //savedBookRequestDto.(user.getId());
//        SavedBookResponseDto savedBookResponse = savedBookService.saveBook(savedBookRequestDto);
//
//        return ResponseEntity.ok("Book saved successfully with ID: " + savedBookResponse.getId());
//    }
//
//    @GetMapping
//    public ResponseEntity<List<SavedBook>> getSavedBooksForCurrentUser() {
//        String userEmail = userService.getAuthUserEmail();
//        User user = userService.findUserByEmail(userEmail);
//
//        List<SavedBook> savedBooks = savedBookService.getAllSavedBooksByUser();
//
//        return ResponseEntity.ok(savedBooks);
//    }
//
//    @DeleteMapping("/{savedBookId}")
//    public ResponseEntity<String> deleteSavedBook(@PathVariable Long savedBookId) {
//        String userEmail = userService.getAuthUserEmail();
//        User user = userService.findUserByEmail(userEmail);
//
//        savedBookService.deleteSavedBookForUser(savedBookId);
//
//        return ResponseEntity.ok("Saved book deleted successfully.");
//    }
//
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<SavedBook>> getSavedBooksByUserId(@PathVariable Long userId) {
//        List<SavedBook> savedBooks = savedBookService.getSavedBooksForUserById(userId);
//
//        return ResponseEntity.ok(savedBooks);
//    }
//}
