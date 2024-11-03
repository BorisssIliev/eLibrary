package com.example.eLibrary.service.book.impl;

import com.example.eLibrary.entity.book.BookImage;
import com.example.eLibrary.repository.book.BookImageRepository;
import com.example.eLibrary.service.book.BookImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookImageServiceImpl implements BookImageService {

    private final BookImageRepository bookImageRepository;

    @Override
    public BookImage saveBookImage(BookImage bookImage) {
        return bookImageRepository.save(bookImage);
    }

    @Override
    public Optional<BookImage> getBookImageById(Long id) {
        return bookImageRepository.findById(id);
    }

    @Override
    public void deleteBookImage(Long id) {
        bookImageRepository.deleteById(id);
    }
}
