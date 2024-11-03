package com.example.eLibrary.repository.book;

import com.example.eLibrary.entity.book.BookImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookImageRepository extends JpaRepository<BookImage, Long> {

}
