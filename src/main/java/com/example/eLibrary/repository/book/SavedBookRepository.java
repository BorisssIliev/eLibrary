package com.example.eLibrary.repository.book;

import com.example.eLibrary.entity.book.SavedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedBookRepository extends JpaRepository<SavedBook, Long> {

    List<SavedBook> findByUserId(Long userId);

}
