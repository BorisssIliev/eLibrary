package com.example.eLibrary.controller.frontEnd;

import com.example.eLibrary.dto.book.CommentRequestDto;
import com.example.eLibrary.service.book.BookService;
import com.example.eLibrary.service.book.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/books")
public class CommentController {

    private final CommentService commentService;
    private final BookService bookService;

    @PostMapping("/{bookId}/comment")
    public String addComment(@PathVariable Long bookId,
                             @ModelAttribute CommentRequestDto commentRequestDto,
                             RedirectAttributes redirectAttributes) {
        try {
            commentService.saveComment(commentRequestDto, bookId);
            redirectAttributes.addFlashAttribute("message", "✅ Коментарът беше добавен успешно!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "❌ Грешка при записването на коментара!");
        }

        return "redirect:/v1/books/" + bookId;
    }

    @PostMapping("/{bookId}/comment/{commentId}/delete")
    public String deleteComment(@PathVariable Long bookId,
                                @PathVariable Long commentId,
                                RedirectAttributes redirectAttributes) {
        try {
            commentService.deleteCommentIfAuthor(commentId);
            redirectAttributes.addFlashAttribute("message", "✅ Коментарът беше изтрит успешно!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "❌ " + e.getMessage());
        }

        return "redirect:/v1/books/" + bookId;
    }


//    @GetMapping("/book/{bookId}")
//    public String getComments(@PathVariable Long bookId, Model model) {
//        Optional<Book> book = bookService.getBookById(bookId);
//        if (book.isEmpty()) {
//            return "redirect:/index";
//        }
//
//        // Извличаме коментарите
//        List<CommentResponseDto> comments = commentService.getCommentsByBook(bookId);
//
//        model.addAttribute("book", book.get());
//        model.addAttribute("comments", comments); // 📌 ВАЖНО! Добавяме коментарите към модела
//
//        return "book-details";
//    }

}
