package com.example.eLibrary.controller.frontEnd;

import com.example.eLibrary.entity.book.Book;
import com.example.eLibrary.service.book.BookService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/v1")
public class BooksCatalogController {

    private final BookService bookService;

    @GetMapping("/catalog")
    public String getCatalogPage(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        List<Book> books = bookService.getAllBooks();

        // Уверете се, че startDate и endDate имат стойности
        final LocalDate effectiveStartDate = (startDate != null) ? startDate : LocalDate.of(1801, 1, 1);
        final LocalDate effectiveEndDate = (endDate != null) ? endDate : LocalDate.now();

        // Прилагайте филтри последователно
        if (title != null && !title.trim().isEmpty()) {
            books = books.stream()
                    .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                    .toList();
        }

        if (author != null && !author.trim().isEmpty()) {
            books = books.stream()
                    .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                    .toList();
        }

        if (isbn != null && !isbn.trim().isEmpty()) {
            books = books.stream()
                    .filter(book -> book.getIsbn().equalsIgnoreCase(isbn))
                    .toList();
        }

        if (genre != null && !genre.trim().isEmpty()) {
            books = books.stream()
                    .filter(book -> genre.equalsIgnoreCase(book.getGenre()))
                    .toList();
        }

        books = books.stream()
                .filter(book -> book.getPublicationDate().isAfter(effectiveStartDate.minusDays(1)) &&
                        book.getPublicationDate().isBefore(effectiveEndDate.plusDays(1)))
                .toList();

        // Добавяне на данни към модела
        model.addAttribute("books", books);
        model.addAttribute("title", title);
        model.addAttribute("author", author);
        model.addAttribute("isbn", isbn);
        model.addAttribute("genre", genre);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "book-catalog";
    }

}
