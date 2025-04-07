package com.example.eLibrary.service.book.impl;

import com.example.eLibrary.dto.book.BookImageResponseDto;
import com.example.eLibrary.entity.book.Book;
import com.example.eLibrary.entity.book.BookImage;
import com.example.eLibrary.exception.ResourceNotFoundException;
import com.example.eLibrary.repository.book.BookImageRepository;
import com.example.eLibrary.repository.book.BookRepository;
import com.example.eLibrary.service.book.BookImageService;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookImageServiceImpl implements BookImageService {

    private final BookImageRepository bookImageRepository;
    private final BookRepository bookRepository;


    @Override
    public BookImage saveBookImage(BookImage bookImage) {
        return bookImageRepository.save(bookImage);
    }

    @Override
    @Transactional
    public BookImageResponseDto uploadImage(MultipartFile file, Long bookId) throws IOException {
        // Намиране на книгата по ID
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Книга с ID %d не е намерена", bookId)));

        // Проверка дали книгата вече има изображение
        if (bookImageRepository.findByBookId(bookId).isPresent()) {
            throw new IllegalStateException("Книгата вече има изображение. Възможно е само едно изображение на книга.");
        }

        // Създаване и запазване на ново изображение
        BookImage newImage = BookImage.builder()
                .imageName(file.getOriginalFilename())
                .book(book)
                .build();

        bookImageRepository.save(newImage);

        // Връщане на DTO отговор за изображението
        return BookImageResponseDto.builder()
                .id(newImage.getId())
                .imageName(newImage.getImageName())
                .bookId(newImage.getBook().getId())
                .build();
    }

    @Override
    public List<BookImage> findByName(String name) {
        return null;
    }

//    @Override
//    public Optional<BookImage> findImageByValue(byte[] value) {
//        return null;
//    }

    @Override
    public BookImage updateBookImage(Long id, BookImage bookImage) {
        return null;
    }

    @Override
    public List<BookImage> getAllImages() {
        return null;
    }



    @Override
    public Optional<List<BookImage>> findByBookId(Long bookId) {
        return Optional.empty();
    }

    @Override
    public BookImage findById(Long imageId) {
        return bookImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Изображение с ID " + imageId + " не е намерено"));
    }

}