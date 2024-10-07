package com.example.eLibrary.repository.user;

import com.example.eLibrary.entity.user.User;
import com.example.eLibrary.entity.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<List<User>> findByFirstName(String firstName);

    Optional<List<User>> findByLastName(String lastName);

    Optional<List<User>> findByFirstNameAndLastName(String firstName, String lastName);

    Optional<User> findByEmail(String email);


    Optional<List<User>> findByUserRole(UserRole userRole);

    boolean existsByEmail(String email);

    boolean existsByFirstName(String firstName);

    boolean existsByLastName(String lastName);
    
}
