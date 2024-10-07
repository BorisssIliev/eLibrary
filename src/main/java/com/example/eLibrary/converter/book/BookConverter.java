package com.example.eLibrary.converter.book;

import com.example.eLibrary.dto.book.BookRequestDto;
import com.example.eLibrary.dto.book.BookResponseDto;
import com.example.eLibrary.entity.book.Book;
import org.springframework.stereotype.Component;

@Component
public class BookConverter {

    public Book toBook(BookRequestDto bookRequestDto) {
        return Book.builder()
                .title(bookRequestDto.getTitle())
                .author(bookRequestDto.getAuthor())
                .genre(bookRequestDto.getGenre())
                .isbn(bookRequestDto.getIsbn())
                .publicationDate(bookRequestDto.getPublicationDate())
                .build();
    }

    public BookResponseDto toBookResponseDto(Book book) {
        return BookResponseDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .genre(book.getGenre())
                .isbn(book.getIsbn())
                .publicationDate(book.getPublicationDate())
                .build();
    }

    public void updateBookFromDto(BookRequestDto bookRequestDto, Book book) {
        book.setTitle(bookRequestDto.getTitle());
        book.setAuthor(bookRequestDto.getAuthor());
        book.setGenre(bookRequestDto.getGenre());
        book.setIsbn(bookRequestDto.getIsbn());
        book.setPublicationDate(bookRequestDto.getPublicationDate());
    }
}
