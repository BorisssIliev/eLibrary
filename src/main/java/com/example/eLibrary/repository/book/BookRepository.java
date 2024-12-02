package com.example.eLibrary.repository.book;

import com.example.eLibrary.entity.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);

    List<Book> findByPublicationDateBetween(Date startDate, Date endDate);

    @Query("SELECT b FROM Book b ORDER BY FUNCTION('RAND')")
    List<Book> getRandomBooks(Pageable pageable);

}
