package com.example.eLibrary.controller.user.service.user;

import com.example.eLibrary.converter.user.UserConverter;
import com.example.eLibrary.dto.user.UpdateUserInfoRequest;
import com.example.eLibrary.dto.user.UserResponse;
import com.example.eLibrary.entity.user.User;
import com.example.eLibrary.payload.response.UserInfoResponse;
import com.example.eLibrary.repository.user.UserRepository;
import com.example.eLibrary.security.AuthenticationService;
import com.example.eLibrary.service.user.impl.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserDetails userDetails;

    @Mock
    private Authentication authentication;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private UserConverter userConverter;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void getUserById() {

        User user = User.builder()
                .firstName("boris")
                .lastName("iliev")
                .email("borisiliev@abv.bg")
                .password("Boris123@")
                .build();


        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> savedUser = userService.getUserById(1L);

        Assertions.assertThat(savedUser).isPresent();
        Assertions.assertThat(savedUser.get()).isNotNull();
        Assertions.assertThat(savedUser.get().getFirstName()).isEqualTo("boris");
        Assertions.assertThat(savedUser.get().getLastName()).isEqualTo("iliev");
        Assertions.assertThat(savedUser.get().getEmail()).isEqualTo("borisiliev@abv.bg");
        Assertions.assertThat(savedUser.get().getPassword()).isEqualTo("Boris123@");

    }

    @Test
    public void getAllUsers() {

        User user1 = User.builder()
                .firstName("Boris")
                .lastName("Iliev")
                .email("borisiliev@abv.bg")
                .password("Boris123@")
                .build();

        User user2 = User.builder()
                .firstName("Ivan")
                .lastName("Petrov")
                .email("ivanpetrov@abv.bg")
                .password("Ivan123@")
                .build();

        List<User> userList = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(userList);

        List<User> allUsers = userService.getAllUsers();

        Assertions.assertThat(allUsers).isNotNull();
        Assertions.assertThat(allUsers.size()).isEqualTo(2);
        Assertions.assertThat(allUsers).containsExactlyInAnyOrder(user1, user2);
    }

    @Test
    public void findByEmail() {

        User user = User.builder()
                .firstName("boris")
                .lastName("iliev")
                .email("borisiliev@abv.bg")
                .password("Boris123@")
                .build();
        
        when(userRepository.findByEmail("borisiliev@abv.bg")).thenReturn(Optional.of(user));

        User foundUser = userService.findUserByEmail("borisiliev@abv.bg");

        Assertions.assertThat(foundUser).isNotNull();
        Assertions.assertThat(foundUser.getEmail()).isEqualTo("borisiliev@abv.bg");
        Assertions.assertThat(foundUser.getFirstName()).isEqualTo("boris");
        Assertions.assertThat(foundUser.getLastName()).isEqualTo("iliev");
    }

    @Test
    public void findByFirstName() {

        User user = User.builder()
                .firstName("boris")
                .lastName("iliev")
                .email("borisiliev@abv.bg")
                .password("Boris123@")
                .build();

        List<User> userList = List.of(user);

        when(userRepository.findByFirstName("boris")).thenReturn(Optional.of(userList));

        Optional<List<User>> foundUsers = userRepository.findByFirstName("boris");

        Assertions.assertThat(foundUsers).isPresent();
        Assertions.assertThat(foundUsers.get()).hasSize(1);
        Assertions.assertThat(foundUsers.get().get(0).getFirstName()).isEqualTo("boris");
        Assertions.assertThat(foundUsers.get().get(0).getEmail()).isEqualTo("borisiliev@abv.bg");
        Assertions.assertThat(foundUsers.get().get(0).getLastName()).isEqualTo("iliev");
    }


    @Test
    public void findByLastName() {

        User user = User.builder()
                .firstName("boris")
                .lastName("iliev")
                .email("borisiliev@abv.bg")
                .password("Boris123@")
                .build();

        List<User> userList = List.of(user);

        when(userRepository.findByLastName("iliev")).thenReturn(Optional.of(userList));

        Optional<List<User>> foundUsers = userRepository.findByLastName("iliev");

        Assertions.assertThat(foundUsers).isPresent();
        Assertions.assertThat(foundUsers.get()).hasSize(1);
        Assertions.assertThat(foundUsers.get().get(0).getLastName()).isEqualTo("iliev");
        Assertions.assertThat(foundUsers.get().get(0).getFirstName()).isEqualTo("boris");
        Assertions.assertThat(foundUsers.get().get(0).getEmail()).isEqualTo("borisiliev@abv.bg");

    }


    @Test
    public void findByFirstAndLastName() {

        User user = User.builder()
                .firstName("boris")
                .lastName("iliev")
                .email("borisiliev@abv.bg")
                .password("Boris123@")
                .build();

        List<User> userList = List.of(user);

        when(userRepository.findByFirstNameAndLastName("boris","iliev")).thenReturn(Optional.of(userList));

        Optional<List<User>> foundUsers = userRepository.findByFirstNameAndLastName("boris","iliev");

        (foundUsers).isPresent();
        Assertions.assertThat(foundUsers.get()).hasSize(1);
        Assertions.assertThat(foundUsers.get().get(0).getLastName()).isEqualTo("iliev");
        Assertions.assertThat(foundUsers.get().get(0).getFirstName()).isEqualTo("boris");
        Assertions.assertThat(foundUsers.get().get(0).getEmail()).isEqualTo("borisiliev@abv.bg");

    }

    @Test
    public void testUpdateUser_SuccessfulUpdate() {

        Long userId = 1L;
        UpdateUserInfoRequest updateRequest = new UpdateUserInfoRequest("NewFirstName", "NewLastName");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("existing@user.com");
        existingUser.setFirstName("OldFirstName");
        existingUser.setLastName("OldLastName");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(authenticationService.getLoggedUserEmail()).thenReturn("existing@user.com");
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        UserInfoResponse expectedResponse = new UserInfoResponse();
        when(userConverter.toResponse(existingUser)).thenReturn(expectedResponse);

        UserResponse actualResponse = userService.updateUser(userId, updateRequest);

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
        Assertions.assertThat(existingUser.getFirstName()).isEqualTo("NewFirstName");
        Assertions.assertThat(existingUser.getLastName()).isEqualTo("NewLastName");

        verify(userRepository, times(1)).findById(userId);
        verify(authenticationService, times(1)).getLoggedUserEmail();
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    public void testUpdateUser_AccessDenied() {

        Long userId = 1L;
        UpdateUserInfoRequest updateRequest = new UpdateUserInfoRequest("NewFirstName", "NewLastName");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("existing@user.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(authenticationService.getLoggedUserEmail()).thenReturn("another@user.com");

        Assertions.assertThatThrownBy(() -> userService.updateUser(userId, updateRequest))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("You do not have permission to update this user's details.");

        verify(userRepository, times(1)).findById(userId);
        verify(authenticationService, times(1)).getLoggedUserEmail();
        verify(userRepository, never()).save(any());
    }

    @Test
    public void testUpdateUser_UserNotFound() {

        Long userId = 1L;
        UpdateUserInfoRequest updateRequest = new UpdateUserInfoRequest("NewFirstName", "NewLastName");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> userService.updateUser(userId, updateRequest))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("There is no user matching id");

        verify(userRepository, times(1)).findById(userId);
        verify(authenticationService, never()).getLoggedUserEmail();
        verify(userRepository, never()).save(any());
    }

    @Test
    public void testDeleteUser() {

        Long userId = 1L;

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void findByUsername() {

        User user = User.builder()
                .firstName("boris")
                .lastName("iliev")
                .email("borisiliev@abv.bg")
                .password("Boris123@")
                .build();

        when(userRepository.findByEmail("borisiliev@abv.bg")).thenReturn(Optional.of(user));

        User foundUser = userService.findUserByEmail("borisiliev@abv.bg");

        Assertions.assertThat(foundUser).isNotNull();
        Assertions.assertThat(foundUser.getEmail()).isEqualTo("borisiliev@abv.bg");
        Assertions.assertThat(foundUser.getFirstName()).isEqualTo("boris");
        Assertions.assertThat(foundUser.getLastName()).isEqualTo("iliev");
    }

    @Test
    public void testGetAuthUserEmail() {
        // Arrange
        String expectedEmail = "testuser@example.com";

        when(userDetails.getUsername()).thenReturn(expectedEmail);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        // Задаваме mock SecurityContext на SecurityContextHolder
        SecurityContextHolder.setContext(securityContext);

        // Act
        String authUserEmail = userService.getAuthUserEmail();

        // Assert
        Assertions.assertThat(authUserEmail).isEqualTo(expectedEmail);
    }

}
