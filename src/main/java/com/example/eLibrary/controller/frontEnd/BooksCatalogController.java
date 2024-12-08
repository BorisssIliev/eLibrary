package com.example.eLibrary.controller.frontEnd;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import com.example.eLibrary.entity.book.Book;
import com.example.eLibrary.service.book.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/v1")
public class BooksCatalogController {

    private final BookService bookService;

    @GetMapping("/catalog")
    public String getCatalogPage(@RequestParam(required = false) String keyword,
                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                 Model model) {
        List<Book> books;

        if ((keyword == null || keyword.trim().isEmpty()) && startDate == null && endDate == null) {
            books = bookService.getAllBooks();
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            books = bookService.searchBooks(keyword);
        } else {
            if (startDate == null && endDate != null) {
                startDate = LocalDate.of(1801, 1, 1);
            } else if (startDate != null && endDate == null) {
                endDate = LocalDate.now();
            } else if (startDate == null && endDate == null) {
                books = bookService.getAllBooks();
                model.addAttribute("books", books);
                model.addAttribute("keyword", keyword);
                model.addAttribute("startDate", startDate);
                model.addAttribute("endDate", endDate);
                return "book-catalog";
            }

            books = bookService.getBooksByPublicationDateRange(startDate, endDate);
        }

        model.addAttribute("books", books);
        model.addAttribute("keyword", keyword);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "book-catalog";
    }
}



