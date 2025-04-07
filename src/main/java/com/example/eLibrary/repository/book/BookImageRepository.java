package com.example.eLibrary.repository.book;

import com.example.eLibrary.entity.book.BookImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface BookImageRepository extends JpaRepository<BookImage, Long> {

    Optional<List<BookImage>> findByBookId(Long bookId);
    Optional<BookImage> findByImageName(String imageName);

}

