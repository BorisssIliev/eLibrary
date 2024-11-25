package com.example.eLibrary.service.book;

import com.example.eLibrary.dto.book.BookImageResponseDto;
import com.example.eLibrary.entity.book.BookImage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Service
public interface BookImageService {

    BookImage saveBookImage(BookImage bookImage);

    Optional<List<BookImage>> findByBookId(Long bookId);

    BookImage findById(Long bookImageId);

    Optional<BookImage> findImageByValue(byte[] value);

    BookImageResponseDto uploadImage(MultipartFile file, Long bookId) throws IOException;

    List<BookImage> findByName(String name);

    BookImage updateBookImage(Long id, BookImage bookImage);

    List<BookImage> getAllImages();


}
