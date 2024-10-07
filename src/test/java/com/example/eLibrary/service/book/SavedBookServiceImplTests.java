package com.example.eLibrary.service.book;

import com.example.eLibrary.converter.book.SaveBookConverter;
import com.example.eLibrary.dto.savedBook.SavedBookRequestDto;
import com.example.eLibrary.dto.savedBook.SavedBookResponseDto;
import com.example.eLibrary.entity.book.Book;
import com.example.eLibrary.entity.book.SavedBook;
import com.example.eLibrary.entity.user.User;
import com.example.eLibrary.exception.UnauthorizedException;
import com.example.eLibrary.repository.book.SavedBookRepository;
import com.example.eLibrary.security.utils.SecurityUtils;
import com.example.eLibrary.service.book.impl.SavedBookServiceImpl;
import com.example.eLibrary.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SavedBookServiceImplTests {

    @InjectMocks
    private SavedBookServiceImpl savedBookService;

    @Mock
    private SavedBookRepository savedBookRepository;

    @Mock
    private SaveBookConverter saveBookConverter;

    @Mock
    private UserService userService;

    @Mock
    private BookService bookService;

    @Mock
    private SecurityUtils securityUtils;

    private User user;
    private Book book;
    private SavedBook savedBook;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .id(1L)
                .email("user@example.com")
                .build();

        book = Book.builder()
                .id(1L)
                .title("Book Title")
                .build();

        savedBook = SavedBook.builder()
                .id(1L)
                .user(user)
                .book(book)
                .savedDate(LocalDate.now())
                .build();
    }

    @Test
    public void testSaveBook() {
        SavedBookRequestDto requestDto = new SavedBookRequestDto();
        SavedBookResponseDto responseDto = new SavedBookResponseDto();

        when(saveBookConverter.toSavedBook(requestDto)).thenReturn(savedBook);
        when(savedBookRepository.save(savedBook)).thenReturn(savedBook);
        when(saveBookConverter.toResponse(savedBook)).thenReturn(responseDto);

        SavedBookResponseDto result = savedBookService.saveBook(requestDto);

        verify(saveBookConverter).toSavedBook(requestDto);
        verify(savedBookRepository).save(savedBook);
        verify(saveBookConverter).toResponse(savedBook);

        assertNotNull(result);
    }

    @Test
    public void testDeleteSavedBook() {
        when(savedBookRepository.findById(1L)).thenReturn(Optional.of(savedBook));

        savedBookService.deleteSavedBook(1L);

        verify(savedBookRepository).delete(savedBook);
    }

    @Test
    public void testDeleteSavedBookForUser_Unauthorized() {
        when(securityUtils.extractUserEmailFromToken()).thenReturn("user@example.com");
        when(userService.findUserByEmail("user@example.com")).thenReturn(user);
        when(savedBookRepository.findById(1L)).thenReturn(Optional.of(savedBook));
        savedBook.setUser(User.builder().id(2L).build()); // Different user

        Exception exception = assertThrows(UnauthorizedException.class, () -> {
            savedBookService.deleteSavedBookForUser(1L);
        });

        assertEquals("You do not have permission to delete this book.", exception.getMessage());
    }

    @Test
    public void testDeleteSavedBookForUser_Success() {
        when(securityUtils.extractUserEmailFromToken()).thenReturn("user@example.com");
        when(userService.findUserByEmail("user@example.com")).thenReturn(user);
        when(savedBookRepository.findById(1L)).thenReturn(Optional.of(savedBook));

        savedBookService.deleteSavedBookForUser(1L);

        verify(savedBookRepository).delete(savedBook);
    }

    @Test
    public void testGetSavedBooksForUserById() {
        when(savedBookRepository.findByUserId(1L)).thenReturn(Collections.singletonList(savedBook));

        List<SavedBook> result = savedBookService.getSavedBooksForUserById(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(savedBook, result.get(0));
    }

    @Test
    public void testGetAllSavedBooksByUser() {
        when(securityUtils.extractUserEmailFromToken()).thenReturn("user@example.com");
        when(userService.findUserByEmail("user@example.com")).thenReturn(user);
        when(savedBookRepository.findByUserId(user.getId())).thenReturn(Collections.singletonList(savedBook));

        List<SavedBook> result = savedBookService.getAllSavedBooksByUser();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(savedBook, result.get(0));
    }
}
