package com.example.eLibrary.service.book;

import com.example.eLibrary.dto.book.BookRequestDto;
import com.example.eLibrary.dto.book.BookResponseDto;
import com.example.eLibrary.entity.book.Book;
import com.example.eLibrary.entity.book.BookImage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public interface BookService {

    Book saveBook(Book book);

    List<Book> getAllBooks();

    Optional<Book> getBookById(Long id);

    public BookResponseDto createBook(BookRequestDto dto, MultipartFile imageFile) throws IOException;

    public BookImage saveImageFile(MultipartFile imageFile, Book book) throws IOException;

    BookResponseDto updateBook(Long id, BookRequestDto bookRequestDto);

    void deleteBook(Long id);

    List<Book> getRandomBooks(int limit);

    boolean existsByTitleAndAuthor(String title, String author);

    List<Book> searchBooksByTitle(String title);

    public List<Book> searchBooksByAuthor(String author);

    public List<Book> searchBooksByIsbn(String isbn);

    public List<Book> searchBooksByGenre(String genre);

    List<Book> getBooksByPublicationDateRange(LocalDate startDate, LocalDate endDate);
}
