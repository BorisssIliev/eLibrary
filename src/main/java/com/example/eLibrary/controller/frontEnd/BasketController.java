package com.example.eLibrary.controller.frontEnd;

import com.example.eLibrary.dto.savedBook.SavedBookRequestDto;
import com.example.eLibrary.dto.savedBook.SavedBookResponseDto;
import com.example.eLibrary.service.book.SavedBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    // GET заявка за показване на запазените книги на текущия потребител
    @GetMapping
    public String viewSavedBooks(Model model) {
        model.addAttribute("savedBooks", savedBookService.getAllSavedBooksByUser());
        return "basket"; // Това трябва да е името на Thymeleaf шаблона за списъка с запазени книги
    }
}