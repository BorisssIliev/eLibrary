package com.example.eLibrary.service.book;

import com.example.eLibrary.dto.book.BookRequestDto;
import com.example.eLibrary.dto.book.BookResponseDto;
import com.example.eLibrary.entity.book.Book;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BookService {

    List<Book> getAllBooks();

    Optional<Book> getBookById(Long id);

    Optional<Book> getBookEntityById(Long id);

    BookResponseDto createBook(BookRequestDto bookRequestDto);

    BookResponseDto updateBook(Long id, BookRequestDto bookRequestDto);

    void deleteBook(Long id);

}
