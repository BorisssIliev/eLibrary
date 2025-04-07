package com.example.eLibrary.service.book.impl;

import com.example.eLibrary.converter.book.BookConverter;
import com.example.eLibrary.dto.book.BookRequestDto;
import com.example.eLibrary.dto.book.BookResponseDto;
import com.example.eLibrary.entity.book.Book;
import com.example.eLibrary.entity.book.BookImage;
import com.example.eLibrary.repository.book.BookRepository;
import com.example.eLibrary.service.book.BookService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        return bookRepository.findAllWithImages();
    }

    @Override
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public BookResponseDto createBook(BookRequestDto dto, MultipartFile imageFile) throws IOException {
        Book book = bookConverter.convertToEntity(dto);

        if (!imageFile.isEmpty()) {
            String uploadsDir = "uploads";
            Path uploadPath = Paths.get(uploadsDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path imagePath = uploadPath.resolve(fileName);

            Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

            BookImage bookImage = new BookImage();
            bookImage.setImageName(fileName);
            bookImage.setBook(book);

            book.getBookImages().add(bookImage);
        }

        bookRepository.save(book);
        return bookConverter.toBookResponseDto(book);
    }

    public BookImage saveImageFile(MultipartFile imageFile, Book book) throws IOException {
        String uploadsDir = "uploads";
        Path uploadPath = Paths.get(uploadsDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        Path imagePath = uploadPath.resolve(fileName);

        Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

        return BookImage.builder()
                .imageName(fileName)
                .book(book)
                .build();
    }




    @Transactional
    @Override
    public BookResponseDto updateBook(Long id, BookRequestDto bookRequestDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        bookConverter.updateBookFromDto(bookRequestDto, book);

        Book saved = bookRepository.save(book);

        return bookConverter.toBookResponseDto(saved);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // 1. Изтрий изображенията от базата
        book.getBookImages().clear(); // ако имаш `cascade = ALL` и `orphanRemoval = true`, това ще ги махне

        // 2. Изтрий самата книга
        bookRepository.delete(book);
    }

    @Override
    public List<Book> getRandomBooks(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return bookRepository.getRandomBooks(pageable);
    }

    @Override
    public boolean existsByTitleAndAuthor(String title, String author) {
        return bookRepository.existsByTitleIgnoreCaseAndAuthorIgnoreCase(title, author);
    }

    @Override
    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public List<Book> searchBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    @Override
    public List<Book> searchBooksByIsbn(String isbn) {
        return bookRepository.findByIsbnContainingIgnoreCase(isbn);
    }

    @Override
    public List<Book> searchBooksByGenre(String genre) {

        if (genre == null || genre.trim().isEmpty()) {
            return bookRepository.findAll();
        }

        return bookRepository.findByGenreContainingIgnoreCase(genre);
    }

    @Override
    public List<Book> getBooksByPublicationDateRange(LocalDate startDate, LocalDate endDate) {
        return bookRepository.findByPublicationDateBetween(startDate, endDate);
    }

}
