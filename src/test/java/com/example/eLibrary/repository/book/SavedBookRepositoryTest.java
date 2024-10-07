package com.example.eLibrary.repository.book;

import com.example.eLibrary.entity.book.Book;
import com.example.eLibrary.entity.book.SavedBook;
import com.example.eLibrary.entity.user.User;
import com.example.eLibrary.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class SavedBookRepositoryTest {

    @Autowired
    private SavedBookRepository savedBookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    private User user;
    private Book book1, book2;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("ValidPass1@!")
                .createdDate(LocalDate.now())
                .build();

        userRepository.saveAndFlush(user);

        book1 = Book.builder()
                .title("Book 1")
                .author("Author 1")
                .genre("Fiction")
                .isbn("1234567890")
                .publicationDate(LocalDate.of(2020, 5, 20))
                .build();

        book2 = Book.builder()
                .title("Book 2")
                .author("Author 2")
                .genre("Science Fiction")
                .isbn("0987654321")
                .publicationDate(LocalDate.of(2018, 3, 15))
                .build();

        bookRepository.saveAndFlush(book1);
        bookRepository.saveAndFlush(book2);

        SavedBook savedBook1 = SavedBook.builder()
                .savedDate(LocalDate.now())
                .user(user)
                .book(book1)
                .build();

        SavedBook savedBook2 = SavedBook.builder()
                .savedDate(LocalDate.now())
                .user(user)
                .book(book2)
                .build();

        savedBookRepository.saveAndFlush(savedBook1);
        savedBookRepository.saveAndFlush(savedBook2);
    }

    @Test
    public void testFindByUserId() {
        List<SavedBook> savedBooks = savedBookRepository.findByUserId(user.getId());

        assertNotNull(savedBooks);
        assertEquals(2, savedBooks.size());
        assertEquals(book1.getTitle(), savedBooks.get(0).getBook().getTitle());
        assertEquals(book2.getTitle(), savedBooks.get(1).getBook().getTitle());
    }
}
