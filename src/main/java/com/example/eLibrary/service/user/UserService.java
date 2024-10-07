package com.example.eLibrary.service.user;

import com.example.eLibrary.dto.user.UpdateUserInfoRequest;
import com.example.eLibrary.dto.user.UserResponse;
import com.example.eLibrary.entity.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    Optional<User> getUserById(Long id);

    User findUserByEmail(String email);

    List<User> getAllUsers();

    List<User> getUserByFirstName(String firstName);

    List<User> getUserByLastName(String lastName);

    List<User> getUserByFirstNameAndLastName(String firstName, String lastName);

    UserResponse updateUser(Long userId, UpdateUserInfoRequest updateUserInfoRequest);

    void deleteUser(Long userId);

    Optional<User> getUserByUsername(String username);

    String getAuthUserEmail();
}
