package com.example.eLibrary.repository.book;

import com.example.eLibrary.entity.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAuthorContainingIgnoreCase(String author);

    List<Book> findByIsbnContainingIgnoreCase(String isbn);

    List<Book> findByGenreContainingIgnoreCase(String genre);

    boolean existsByTitleIgnoreCaseAndAuthorIgnoreCase(String title, String author);

    List<Book> findByPublicationDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.bookImages")
    List<Book> findAllWithImages();

    @Query("SELECT b FROM Book b ORDER BY FUNCTION('RAND')")
    List<Book> getRandomBooks(Pageable pageable);


}
