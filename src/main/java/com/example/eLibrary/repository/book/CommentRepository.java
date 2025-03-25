package com.example.eLibrary.repository.book;

import com.example.eLibrary.entity.book.Book;
import com.example.eLibrary.entity.book.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByBookIdOrderByCreatedAtDesc(Long bookId);

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.comments WHERE b.id = :bookId")
    Optional<Book> findBookWithComments(@Param("bookId") Long bookId);
}
