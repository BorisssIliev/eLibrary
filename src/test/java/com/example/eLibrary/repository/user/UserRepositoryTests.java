package com.example.eLibrary.repository.user;

import com.example.eLibrary.entity.user.User;
import com.example.eLibrary.entity.user.UserRole;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private UserRole userRoleUser;
    private UserRole userRoleAdmin;

    private User user1;
    private User user2;

    @BeforeEach
    public void setup() {

        userRoleUser = UserRole.builder()
                .userRole("USER")
                .build();

        userRoleAdmin = UserRole.builder()
                .userRole("ADMIN")
                .build();

        entityManager.persist(userRoleUser);
        entityManager.persist(userRoleAdmin);

        // Подготви тестови данни
        user1 = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("Boris987@")
                .createdDate(LocalDate.now())
                .userRole(userRoleUser)
                .build();

        user2 = User.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .password("Boris987@")
                .createdDate(LocalDate.now())
                .userRole(userRoleAdmin)
                .build();

        userRepository.save(user1);
        userRepository.save(user2);
    }

    @Test
    public void testFindByFirstName() {
        Optional<List<User>> users = userRepository.findByFirstName("John");
        assertTrue(users.isPresent());
        assertEquals(1, users.get().size());
        assertEquals("john.doe@example.com", users.get().get(0).getEmail());
    }

    @Test
    public void testFindByLastName() {
        Optional<List<User>> users = userRepository.findByLastName("Doe");
        assertTrue(users.isPresent());
        assertEquals(2, users.get().size());  // Очаква 2 потребители с фамилия "Doe"
    }

    @Test
    public void testFindByFirstNameAndLastName() {
        Optional<List<User>> users = userRepository.findByFirstNameAndLastName("John", "Doe");
        assertTrue(users.isPresent());
        assertEquals(1, users.get().size());
        assertEquals("john.doe@example.com", users.get().get(0).getEmail());
    }

    @Test
    public void testFindByEmail() {
        Optional<User> user = userRepository.findByEmail("jane.doe@example.com");
        assertTrue(user.isPresent());
        assertEquals("Jane", user.get().getFirstName());
    }

    @Test
    public void testExistsByEmail() {
        boolean exists = userRepository.existsByEmail("john.doe@example.com");
        assertTrue(exists);

        boolean notExists = userRepository.existsByEmail("nonexistent@example.com");
        assertFalse(notExists);
    }

    @Test
    public void testFindByUserRole() {
        Optional<List<User>> users = userRepository.findByUserRole(userRoleAdmin);
        assertTrue(users.isPresent());
        assertEquals(1, users.get().size());
        assertEquals("jane.doe@example.com", users.get().get(0).getEmail());
    }

    @Test
    public void testExistsByFirstName() {
        boolean exists = userRepository.existsByFirstName("John");
        assertTrue(exists);

        boolean notExists = userRepository.existsByFirstName("Nonexistent");
        assertFalse(notExists);
    }

    @Test
    public void testExistsByLastName() {
        boolean exists = userRepository.existsByLastName("Doe");
        assertTrue(exists);

        boolean notExists = userRepository.existsByLastName("Smith");
        assertFalse(notExists);
    }
}

