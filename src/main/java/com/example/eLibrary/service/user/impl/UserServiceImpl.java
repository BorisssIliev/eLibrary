package com.example.eLibrary.service.user.impl;

import com.example.eLibrary.converter.user.UserConverter;
import com.example.eLibrary.dto.user.UpdateUserInfoRequest;
import com.example.eLibrary.dto.user.UserResponse;
import com.example.eLibrary.entity.user.User;
import com.example.eLibrary.exception.ResourceNotFoundException;
import com.example.eLibrary.repository.user.UserRepository;
import com.example.eLibrary.security.AuthenticationService;
import com.example.eLibrary.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    private final UserConverter userConverter;

    @Autowired
    private final AuthenticationService authenticationService;

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @Override
    public User findUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElseThrow(() -> new ResourceNotFoundException("User with this email not found"));
    }

    @Override
    public List<User> getUserByFirstName(String firstName) {
        return userRepository.findByFirstName(firstName)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("There is no users with that first name %s", firstName)));
    }

    @Override
    public List<User> getUserByLastName(String lastName) {
        return userRepository.findByLastName(lastName)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("There is no users with that first name %s", lastName)));
    }

    @Override
    public List<User> getUserByFirstNameAndLastName(String firstName, String lastName) {
        return userRepository.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with first name %s and laslt name %s not found", firstName, lastName)));
    }

    public UserResponse updateUser(Long userId, UpdateUserInfoRequest updateUserInfoRequest) {
        User updatedUser = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException(String.format("There is no user matching id %s.", userId)));
        if (authenticationService.getLoggedUserEmail().equals(updatedUser.getEmail())) {
            updatedUser.setFirstName(updateUserInfoRequest.getFirstName());
            updatedUser.setLastName(updateUserInfoRequest.getLastName());
        } else {
            throw new AccessDeniedException("You do not have permission to update this user's details.");
        }
        return userConverter.toUserResponse(userRepository.save(updatedUser));
    }


    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }


    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByEmail(username);
    }

    @Override
    public String getAuthUserEmail() {
        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String authUserEmail = authUser.getUsername();
        return authUserEmail;

    }
}