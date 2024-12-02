package com.example.eLibrary.service.book.impl;

import com.example.eLibrary.converter.book.BookConverter;
import com.example.eLibrary.dto.book.BookRequestDto;
import com.example.eLibrary.dto.book.BookResponseDto;
import com.example.eLibrary.entity.book.Book;
import com.example.eLibrary.repository.book.BookRepository;
import com.example.eLibrary.service.book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookConverter bookConverter;

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public BookResponseDto createBook(BookRequestDto bookRequestDto) {
        Book book = bookConverter.toBook(bookRequestDto);
        bookRepository.save(book);
        return bookConverter.toBookResponseDto(book);
    }

    @Override
    public BookResponseDto updateBook(Long id, BookRequestDto bookRequestDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        bookConverter.updateBookFromDto(bookRequestDto, book);
        bookRepository.save(book);
        return bookConverter.toBookResponseDto(book);
    }

    @Override
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        bookRepository.delete(book);
    }

    @Override
    public List<Book> getRandomBooks(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return bookRepository.getRandomBooks(pageable);
    }

    @Override
    public List<Book> searchBooks(String keyword) {
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
    }

    @Override
    public List<Book> getBooksByPublicationDateRange(Date startDate, Date endDate) {
        return bookRepository.findByPublicationDateBetween(startDate, endDate);
    }
}
