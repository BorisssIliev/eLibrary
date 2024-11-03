package com.example.eLibrary.controller.frontEnd;

import com.example.eLibrary.entity.book.Book;
import com.example.eLibrary.payload.request.LoginRequest;
import com.example.eLibrary.payload.response.AuthResponse;
import com.example.eLibrary.security.AuthenticationService;
import com.example.eLibrary.service.book.BookService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/v1")
public class IndexController {

    private final BookService bookService;

    @GetMapping("/index")
    public String showHomePage(Model model) {

        List<Long> bookIds = Arrays.asList(3L, 4L, 9L, 10L);
        List<Book> books = new ArrayList<>();


        for (Long id : bookIds) {
            bookService.getBookById(id).ifPresent(books::add);
        }

        model.addAttribute("books", books);
        return "index";
    }
}