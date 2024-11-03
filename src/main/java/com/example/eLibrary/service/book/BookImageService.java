package com.example.eLibrary.service.book;

import com.example.eLibrary.entity.book.BookImage;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface BookImageService {

    BookImage saveBookImage(BookImage bookImage);

    // Метод за извличане на снимка по ID
    Optional<BookImage> getBookImageById(Long id);

    // Метод за изтриване на снимка по ID
    void deleteBookImage(Long id);


}
