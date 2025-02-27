package com.example.eLibrary.controller.frontEnd;

import com.example.eLibrary.entity.book.Book;
import com.example.eLibrary.service.book.BookService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.format.DateTimeFormatter;

@Controller
@AllArgsConstructor
@RequestMapping("/v1/books")
public class BookDetailsController {

    private final BookService bookService;

    @GetMapping("/{id}")
    public String getBookDetails(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id)
                .orElseThrow(() -> new RuntimeException("Книгата не е намерена!"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean loggedIn = authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);

        model.addAttribute("book", book);
        model.addAttribute("loggedIn", loggedIn);
        // Добави и други нужни атрибути, например formattedDate, ако е необходимо
        return "book-details";
    }
}

