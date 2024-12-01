package com.example.eLibrary.controller.frontEnd;

import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import com.example.eLibrary.entity.book.Book;
import com.example.eLibrary.service.book.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/v1")
public class BooksCatalogController {

    private final BookService bookService;

    @GetMapping("/catalog")
    public String getCatalogPage(Model model) {
        List<Book> books = bookService.getAllBooks(); // Методът, който връща всички книги
        model.addAttribute("books", books);
        return "book-catalog"; // Името на Thymeleaf шаблона (напр. catalog.html)
    }
}