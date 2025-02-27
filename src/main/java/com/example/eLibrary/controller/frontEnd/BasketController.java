package com.example.eLibrary.controller.frontEnd;

import com.example.eLibrary.dto.savedBook.SavedBookRequestDto;
import com.example.eLibrary.dto.savedBook.SavedBookResponseDto;
import com.example.eLibrary.exception.ResourceNotFoundException;
import com.example.eLibrary.exception.UnauthorizedException;
import com.example.eLibrary.service.book.SavedBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/saved-books")
public class BasketController {

    private final SavedBookService savedBookService;

    // POST заявка за запазване на книга от фронтенда
    @PostMapping("/save")
    public String saveBook(@RequestParam("bookId") Long bookId,
                           RedirectAttributes redirectAttributes) {
        SavedBookRequestDto requestDto = SavedBookRequestDto.builder()
                .bookId(bookId)
                .build();
        try {
            // Опит за запазване на книгата чрез service слоя
            SavedBookResponseDto responseDto = savedBookService.saveBook(requestDto);
            redirectAttributes.addFlashAttribute("message", "Книгата е запазена успешно!");
        } catch (Exception e) {
            // Ако възникне грешка, добавяме съобщение за грешка
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        // Пренасочваме потребителя обратно към страницата с детайлите на книгата
        return "redirect:/v1/books/" + bookId;
    }

    @PostMapping("/basket/remove/{id}")
    public String removeSavedBook(@PathVariable("id") Long savedBookId,
                                  RedirectAttributes redirectAttributes) {
        try {
            savedBookService.deleteSavedBookForUser(savedBookId);
            redirectAttributes.addFlashAttribute("message", "Книгата беше успешно премахната от запазените.");
        } catch (ResourceNotFoundException ex) {
            redirectAttributes.addFlashAttribute("error", "Запазената книга не беше намерена.");
        } catch (UnauthorizedException ex) {
            redirectAttributes.addFlashAttribute("error", "Нямате права да премахнете тази книга.");
        }
        // Може да пренасочите към страницата с всички запазени книги или към index
        return "redirect:/v1/saved-books";
    }

    // GET заявка за показване на запазените книги на текущия потребител
    @GetMapping
    public String viewSavedBooks(Model model) {
        model.addAttribute("savedBooks", savedBookService.getAllSavedBooksByUser());
        return "basket"; // Това трябва да е името на Thymeleaf шаблона за списъка с запазени книги
    }
}