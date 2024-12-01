package com.example.eLibrary.controller.frontEnd;

import com.example.eLibrary.entity.book.Book;
import com.example.eLibrary.service.book.BookService;
import lombok.AllArgsConstructor;
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
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));
        String formattedDate = book.getPublicationDate() != null
                ? book.getPublicationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                : "Unknown";
        model.addAttribute("book", book);
        model.addAttribute("formattedDate", formattedDate);
        return "book-details";
    }
}

