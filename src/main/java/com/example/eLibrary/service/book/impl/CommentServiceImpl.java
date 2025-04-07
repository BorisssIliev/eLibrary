package com.example.eLibrary.service.book.impl;

import com.example.eLibrary.dto.book.CommentRequestDto;
import com.example.eLibrary.dto.book.CommentResponseDto;
import com.example.eLibrary.entity.book.Book;
import com.example.eLibrary.entity.book.Comment;
import com.example.eLibrary.entity.user.User;
import com.example.eLibrary.repository.book.CommentRepository;
import com.example.eLibrary.service.book.BookService;
import com.example.eLibrary.service.book.CommentService;
import com.example.eLibrary.service.user.UserService;
import com.example.eLibrary.security.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BookService bookService;
    private final UserService userService;
    private final SecurityUtils securityUtils;

    @Override
    public Comment saveComment(CommentRequestDto commentRequestDto, Long bookId) {
        try {
            // 1. Извличане на потребителя
            String userEmail = securityUtils.extractUserEmailFromToken();
            User user = userService.findUserByEmail(userEmail);
            if (user == null) {
                throw new RuntimeException("❌ Потребителят не е намерен!");
            }

            // 2. Извличане на книгата
            Book book = bookService.getBookById(bookId)
                    .orElseThrow(() -> new RuntimeException("❌ Книгата не е намерена!"));

            // 3. Проверка дали content не е празен
            if (commentRequestDto.getContent() == null || commentRequestDto.getContent().trim().isEmpty()) {
                throw new RuntimeException("❌ Коментарът не може да бъде празен!");
            }

            // 4. Създаване на коментар
            Comment comment = Comment.builder()
                    .content(commentRequestDto.getContent())
                    .user(user)
                    .book(book)
                    .createdAt(LocalDateTime.now())
                    .build();

            return commentRepository.save(comment);
        } catch (Exception e) {
            e.printStackTrace(); // Логваме грешката в конзолата
            throw new RuntimeException("❌ Грешка при записването на коментара!", e);
        }
    }


    @Override
    public List<CommentResponseDto> getCommentsByBook(Long bookId) {
        List<Comment> comments = commentRepository.findByBookIdOrderByCreatedAtDesc(bookId);

        if (comments.isEmpty()) {
            System.out.println("❌ Няма коментари за книгата с ID: " + bookId);
        } else {
            System.out.println("✅ Намерени коментари: " + comments.size());
            comments.forEach(comment ->
                    System.out.println(comment.getUser().getFirstName() + ": " + comment.getContent()));
        }

        String currentEmail = securityUtils.extractUserEmailFromToken();

        return comments.stream()
                .map(comment -> CommentResponseDto.builder()
                        .id(comment.getId())
                        .authorName(comment.getUser().getFirstName() + " " + comment.getUser().getLastName())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .isAuthor(comment.getUser().getEmail().equals(currentEmail))
                        .build())
                .toList();
    }


    @Override
    @Transactional
    public void deleteCommentIfAuthor(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Коментарът не е намерен."));

        String userEmail = securityUtils.extractUserEmailFromToken();
        if (!comment.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Нямате право да изтриете този коментар.");
        }

        commentRepository.delete(comment);
    }

    @Override
    public void deleteCommentById(Long id) {
        commentRepository.deleteById(id);
    }


}
