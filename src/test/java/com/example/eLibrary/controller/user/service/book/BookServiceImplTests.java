package com.example.eLibrary.controller.user.service.book;

import com.example.eLibrary.converter.book.BookConverter;
import com.example.eLibrary.dto.book.BookRequestDto;
import com.example.eLibrary.dto.book.BookResponseDto;
import com.example.eLibrary.entity.book.Book;
import com.example.eLibrary.repository.book.BookRepository;
import com.example.eLibrary.service.book.impl.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class BookServiceImplTests {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookConverter bookConverter;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void testGetAllBooks() {
        // Arrange
        Book book1 = new Book();
        Book book2 = new Book();
        List<Book> books = List.of(book1, book2);
        when(bookRepository.findAll()).thenReturn(books);

        // Act
        List<Book> result = bookService.getAllBooks();

        // Assert
        assertEquals(books, result);
        verify(bookRepository).findAll();
    }

    @Test
    void testGetBookByIdFound() {
        // Arrange
        Long id = 1L;
        Book book = new Book();
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        // Act
        Optional<Book> result = bookService.getBookById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(book, result.get());
        verify(bookRepository).findById(id);
    }

    @Test
    void testGetBookByIdNotFound() {
        // Arrange
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Book> result = bookService.getBookById(id);

        // Assert
        assertFalse(result.isPresent());
        verify(bookRepository).findById(id);
    }

    @Test
    void testCreateBook() {
        // Arrange
        BookRequestDto bookRequestDto = new BookRequestDto();
        Book book = new Book();
        BookResponseDto bookResponseDto = new BookResponseDto();
        when(bookConverter.toBook(bookRequestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookConverter.toBookResponseDto(book)).thenReturn(bookResponseDto);

        // Act
        BookResponseDto result = bookService.createBook(bookRequestDto);

        // Assert
        assertEquals(bookResponseDto, result);
        verify(bookConverter).toBook(bookRequestDto);
        verify(bookRepository).save(book);
        verify(bookConverter).toBookResponseDto(book);
    }

    @Test
    void testUpdateBook() {
        // Arrange
        Long id = 1L;
        BookRequestDto bookRequestDto = new BookRequestDto();
        Book book = new Book();
        BookResponseDto bookResponseDto = new BookResponseDto();
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookConverter.toBookResponseDto(book)).thenReturn(bookResponseDto);

        // Act
        BookResponseDto result = bookService.updateBook(id, bookRequestDto);

        // Assert
        assertEquals(bookResponseDto, result);
        verify(bookRepository).findById(id);
        verify(bookConverter).updateBookFromDto(bookRequestDto, book);
        verify(bookRepository).save(book);
    }

    @Test
    void testDeleteBook() {
        // Arrange
        Long id = 1L;
        Book book = new Book();
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        // Act
        bookService.deleteBook(id);

        // Assert
        verify(bookRepository).findById(id);
        verify(bookRepository).delete(book);
    }

    @Test
    void testDeleteBookNotFound() {
        // Arrange
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> bookService.deleteBook(id));
        assertEquals("Book not found", thrown.getMessage());
        verify(bookRepository).findById(id);
        verify(bookRepository, never()).delete(any());
    }
}