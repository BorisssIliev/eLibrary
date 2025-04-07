package com.example.eLibrary.controller.book;

import com.example.eLibrary.dto.book.BookRequestDto;
import com.example.eLibrary.dto.book.BookResponseDto;
import com.example.eLibrary.entity.book.Book;
import com.example.eLibrary.security.jwt.JwtService;
import com.example.eLibrary.service.book.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers =BookController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class BookControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private JwtService jwtService;

    @InjectMocks
    private BookController bookController;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
        objectMapper = new ObjectMapper();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

//    @Test
//    public void testCreateBook() throws Exception {
//        // Подготовка на фалшив отговор от сървиса
//        BookResponseDto bookResponseDto = BookResponseDto.builder()
//                .id(1L)
//                .title("Test Title")
//                .author("Test Author")
//                .genre("Test Genre")
//                .isbn("1234567890")
//                .publicationDate(LocalDate.of(2021, 1, 1))
//                .build();
//
//        when(bookService.createBook(any(BookRequestDto.class))).thenReturn(bookResponseDto);
//
//        // Примерна заявка
//        BookRequestDto bookRequestDto = BookRequestDto.builder()
//                .title("Test Title")
//                .author("Test Author")
//                .genre("Test Genre")
//                .isbn("1234567890")
//                .publicationDate(LocalDate.of(2021, 1, 1))
//                .build();
//
//        mockMvc.perform(post("/api/v1/admin/add-book")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(bookRequestDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.title").value("Test Title"));
//    }

    @Test
    public void testGetAllBooks() throws Exception {
        Book book = Book.builder()
                .id(1L)
                .title("Test Title")
                .author("Test Author")
                .genre("Test Genre")
                .isbn("1234567890")
                .publicationDate(LocalDate.of(2021, 1, 1))
                .build();

        when(bookService.getAllBooks()).thenReturn(Collections.singletonList(book));

        mockMvc.perform(get("/api/v1/auth/books/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Test Title"));
    }

    @Test
    public void testGetBookById() throws Exception {
        Book book = Book.builder()
                .id(1L)
                .title("Test Title")
                .author("Test Author")
                .genre("Test Genre")
                .isbn("1234567890")
                .publicationDate(LocalDate.of(2021, 1, 1))
                .build();

        when(bookService.getBookById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(get("/api/v1/auth/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Title"));
    }

    @Test
    public void testUpdateBook() throws Exception {
        BookResponseDto updatedBook = BookResponseDto.builder()
                .id(1L)
                .title("Updated Title")
                .author("Updated Author")
                .genre("Updated Genre")
                .isbn("1234567890")
                .publicationDate(LocalDate.of(2021, 1, 1))
                .build();

        when(bookService.updateBook(eq(1L), any(BookRequestDto.class))).thenReturn(updatedBook);

        BookRequestDto bookRequestDto = BookRequestDto.builder()
                .title("Updated Title")
                .author("Updated Author")
                .genre("Updated Genre")
                .isbn("1234567890")
                .publicationDate(LocalDate.of(2021, 1, 1))
                .build();

        mockMvc.perform(put("/api/v1/admin/books/update-book/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    public void testDeleteBookById() throws Exception {
        mockMvc.perform(delete("/api/v1/admin/books/delete-by-id/1"))
                .andExpect(status().isNoContent());
    }
}

