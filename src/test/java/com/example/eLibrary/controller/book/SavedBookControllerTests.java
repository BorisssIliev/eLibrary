package com.example.eLibrary.controller.book;

import com.example.eLibrary.converter.book.SaveBookConverter;
import com.example.eLibrary.dto.savedBook.SavedBookRequestDto;
import com.example.eLibrary.dto.savedBook.SavedBookResponseDto;
import com.example.eLibrary.entity.book.Book;
import com.example.eLibrary.entity.book.SavedBook;
import com.example.eLibrary.entity.user.User;
import com.example.eLibrary.entity.user.UserRole;
import com.example.eLibrary.security.jwt.JwtService;
import com.example.eLibrary.service.book.SavedBookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SavedBookController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class SavedBookControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SavedBookService savedBookService;

    @MockBean
    private SaveBookConverter saveBookConverter;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private SavedBookController savedBookController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(savedBookController).build();
    }

    @Test
    public void testSaveBook() throws Exception {
        UserRole userRole = new UserRole();
        userRole.setUserRole("USER"); // Уверете се, че задавате стойност, която не е null

        User user = new User();
        user.setUserRole(userRole); // Свържете роля към потребителя

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");

        SavedBookResponseDto savedBookResponseDto = SavedBookResponseDto.builder()
                .id(1L)
                .book(book)
                .user(user)
                .savedDate(LocalDate.now())
                .build();

        when(savedBookService.saveBook(any(SavedBookRequestDto.class))).thenReturn(savedBookResponseDto);

        SavedBookRequestDto savedBookRequestDto = SavedBookRequestDto.builder()
                .bookId(1L)
                .build();

        mockMvc.perform(post("/api/v1/user/saved-book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedBookRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.book.title").value("Test Book"));
    }


    @Test
    public void testGetBySavedBooksByUserId() throws Exception {

        Book book = Book.builder()
                .id(1L)
                .title("Test Book")
                .author("Test Author")
                .build();

        UserRole userRole = UserRole.builder()
                .id(1L)
                .userRole("USER")
                .build();

        User user = User.builder()
                .id(1L)
                .firstName("testuser")
                .userRole(userRole)
                .build();


        SavedBook savedBook = SavedBook.builder()
                .id(1L)
                .book(book)
                .user(user)
                .savedDate(LocalDate.now())
                .build();

        when(savedBookService.getSavedBooksForUserById(anyLong())).thenReturn(Collections.singletonList(savedBook));

        mockMvc.perform(get("/api/v1/admin/saved-book/get-by-user-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].book.title").value("Test Book"))
                .andExpect(jsonPath("$[0].user.firstName").value("testuser"))
                .andExpect(jsonPath("$[0].user.userRole.userRole").value("USER"));
    }


    @Test
    public void testGetByUserToken() throws Exception {
        UserRole userRole = new UserRole();
        userRole.setUserRole("USER_ROLE");

        User user = new User();
        user.setUserRole(userRole);

        Book book = new Book();

        SavedBook savedBook = SavedBook.builder()
                .id(1L)
                .book(book)
                .user(user)
                .savedDate(LocalDate.now())
                .build();

        when(savedBookService.getAllSavedBooksByUser()).thenReturn(Collections.singletonList(savedBook));

        mockMvc.perform(get("/api/v1/user/saved-book/get-by-user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }


    @Test
    public void testDeleteSavedBook() throws Exception {
        mockMvc.perform(delete("/api/v1/user/saved-book/delete-by-user/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteSavedBookById() throws Exception {
        mockMvc.perform(delete("/api/v1/admin/saved-book/delete-by-id/1"))
                .andExpect(status().isNoContent());
    }
}

