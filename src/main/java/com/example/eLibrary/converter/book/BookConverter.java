package com.example.eLibrary.converter.book;

import com.example.eLibrary.dto.book.BookRequestDto;
import com.example.eLibrary.dto.book.BookResponseDto;
import com.example.eLibrary.entity.book.Book;
import com.example.eLibrary.entity.book.BookImage;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class BookConverter {

    public Book convertToEntity(BookRequestDto dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setGenre(dto.getGenre());
        book.setIsbn(dto.getIsbn());
        book.setPublicationDate(dto.getPublicationDate());
        book.setSummary(dto.getSummary());

        book.setBookImages(new ArrayList<>());
        return book;
    }


    public BookResponseDto toBookResponseDto(Book book) {
        return BookResponseDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .genre(book.getGenre())
                .isbn(book.getIsbn())
                .publicationDate(book.getPublicationDate())
                .summary(book.getSummary())
                .build();
    }

    public void updateBookFromDto(BookRequestDto dto, Book book) {
        System.out.println("➡️ DTO Title: " + dto.getTitle());

        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setGenre(dto.getGenre());
        book.setIsbn(dto.getIsbn());
        book.setPublicationDate(dto.getPublicationDate());
        book.setSummary(dto.getSummary());
    }


    public BookRequestDto toBookRequestDto(Book book) {
        return BookRequestDto.builder()
                .title(book.getTitle())
                .author(book.getAuthor())
                .genre(book.getGenre())
                .isbn(book.getIsbn())
                .publicationDate(book.getPublicationDate())
                .summary(book.getSummary())
                .build();
    }

}
