package com.example.eLibrary.service.book;

import com.example.eLibrary.dto.book.CommentRequestDto;
import com.example.eLibrary.dto.book.CommentResponseDto;
import com.example.eLibrary.entity.book.Book;
import com.example.eLibrary.entity.book.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Comment saveComment(CommentRequestDto commentRequestDto, Long bookId);

    List<CommentResponseDto> getCommentsByBook(Long bookId);

    public void deleteCommentIfAuthor(Long commentId);

    void deleteCommentById(Long id);
}
