package com.example.eLibrary.controller.frontEnd;

import com.example.eLibrary.converter.book.BookConverter;
import com.example.eLibrary.dto.book.BookRequestDto;
import com.example.eLibrary.entity.book.Book;
import com.example.eLibrary.entity.book.BookImage;
import com.example.eLibrary.service.book.BookImageService;
import com.example.eLibrary.service.book.BookService;
import com.example.eLibrary.service.book.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/admin")
public class AdminController {

    private final BookService bookService;
    private final BookImageService bookImageService;
    private final BookConverter bookConverter;
    private final CommentService commentService;

    @GetMapping
    public String adminDashboard(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "admin/dashboard";
    }

    @GetMapping("/book/{id}/edit")
    public String editBookForm(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id)
                .orElseThrow(() -> new RuntimeException("Книгата не е намерена."));

        BookRequestDto dto = BookRequestDto.builder()
                .title(book.getTitle())
                .author(book.getAuthor())
                .genre(book.getGenre())
                .isbn(book.getIsbn())
                .summary(book.getSummary())
                .publicationDate(book.getPublicationDate())
                .build();

        model.addAttribute("book", dto);
        model.addAttribute("bookId", book.getId());
        model.addAttribute("comments", book.getComments()); // 👈 подай коментарите

        return "admin/edit-book";
    }

    @PostMapping("/book/{id}/edit")
    public String editBook(@PathVariable Long id,
                           @ModelAttribute("book") BookRequestDto updatedBook,
                           RedirectAttributes redirectAttributes) {
        try {
            bookService.updateBook(id, updatedBook);
            redirectAttributes.addFlashAttribute("message", "✅ Книгата е обновена успешно!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "❌ Грешка при редакцията на книгата.");
        }
        return "redirect:/v1/admin";
    }

    @GetMapping("/book/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new BookRequestDto());
        return "admin/add-book";
    }

    // 👉 POST метод за добавяне на книга + снимка
    @PostMapping("/book/add")
    public String createBook(
            @ModelAttribute("book") BookRequestDto bookRequestDto,
            @RequestParam("imageFiles") MultipartFile[] imageFiles
    ) throws IOException {

        // 1. Създаваме книгата
        Book book = bookService.saveBook(bookConverter.convertToEntity(bookRequestDto));

        // 2. Записваме изображенията
        for (MultipartFile file : imageFiles) {
            if (!file.isEmpty()) {
                BookImage image = bookService.saveImageFile(file, book); // тук имаш логиката в BookServiceImpl
                bookImageService.saveBookImage(image); // запис в базата
            }
        }

        return "redirect:/v1/admin";
    }

    @PostMapping("/book/{id}/delete")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookService.deleteBook(id);
            redirectAttributes.addFlashAttribute("message", "✅ Книгата беше успешно изтрита.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "❌ Неуспешно изтриване на книгата.");
        }
        return "redirect:/v1/admin";
    }

    @PostMapping("/comment/{id}/delete")
    public String deleteComment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            commentService.deleteCommentById(id); // 👈 имплементация в service
            redirectAttributes.addFlashAttribute("message", "✅ Коментарът е изтрит.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "❌ Неуспешно изтриване на коментар.");
        }
        return "redirect:/v1/admin"; // можеш да го промениш към предишната страница с JS ако искаш
    }


}
