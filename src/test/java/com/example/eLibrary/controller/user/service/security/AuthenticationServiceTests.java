package com.example.eLibrary.controller.user.service.security;

import com.example.eLibrary.dto.user.UpdatePasswordRequest;
import com.example.eLibrary.entity.user.User;
import com.example.eLibrary.entity.user.UserRole;
import com.example.eLibrary.payload.request.LoginRequest;
import com.example.eLibrary.payload.request.SignupRequest;
import com.example.eLibrary.payload.response.AuthResponse;
import com.example.eLibrary.repository.user.UserRepository;
import com.example.eLibrary.repository.user.UserRoleRepository;
import com.example.eLibrary.security.AuthenticationService;
import com.example.eLibrary.security.jwt.JwtService;
import com.example.eLibrary.security.services.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTests {

    @Mock
    private UserRoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void testRegister() {
        SignupRequest signupRequest = SignupRequest.builder()
                .firstName("Atanas")
                .lastName("Krastev")
                .email("nasko@gmail.com")
                .password("passWord12@")
                .build();

        UserRole userRole = UserRole.builder().userRole("ROLE_USER").build();
        User user = User.builder()
                .firstName(signupRequest.getFirstName())
                .lastName(signupRequest.getLastName())
                .email(signupRequest.getEmail())
                .password("encodedPassword")
                .userRole(userRole)
                .build();

        when(roleRepository.findByUserRole("ROLE_USER")).thenReturn(userRole);
        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(ArgumentMatchers.<User>any())).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("dummy-token");

        AuthResponse response = authenticationService.register(signupRequest);

        assertEquals("dummy-token", response.getToken());
        verify(userRepository).save(ArgumentMatchers.<User>any());
        verify(jwtService).generateToken(user);
    }

    @Test
    void testLogin() {

        LoginRequest loginRequest = LoginRequest.builder()
                .email("nasko@gmail.com")
                .password("passWord12@")
                .build();

        User user = User.builder()
                .email(loginRequest.getEmail())
                .password("encodedPassword")
                .build();

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(ArgumentMatchers.<Authentication>any())).thenReturn(authentication);

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));

        when(jwtService.generateToken(user)).thenReturn("dummy-token");

        AuthResponse response = authenticationService.login(loginRequest);

        assertEquals("dummy-token", response.getToken());

        verify(authenticationManager).authenticate(ArgumentMatchers.<Authentication>any());
        verify(jwtService, times(2)).generateToken(user); // Променено на times(2)
    }




    @Test
    void testUpdatePasswordSuccess() {
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest("currentPass", "newPass");
        User user = User.builder().email("nasko@gmail.com").password("encodedCurrentPass").build();

        when(userRepository.findByEmail("nasko@gmail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("currentPass", "encodedCurrentPass")).thenReturn(true);
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        authenticationService.updatePassword(updatePasswordRequest);

        verify(userRepository).save(user);
        assertEquals("encodedNewPass", user.getPassword());
    }


    @Test
    void testUpdatePasswordFailure() {
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest("wrongPass", "newPass");
        User user = User.builder().email("nasko@gmail.com").password("encodedCurrentPass").build();

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail("nasko@gmail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPass", "encodedCurrentPass")).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> authenticationService.updatePassword(updatePasswordRequest));
        verify(userRepository, never()).save(ArgumentMatchers.<User>any());
    }

}
