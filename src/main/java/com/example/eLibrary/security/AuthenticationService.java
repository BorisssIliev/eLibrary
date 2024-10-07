package com.example.eLibrary.security;

import com.example.eLibrary.dto.user.UpdatePasswordRequest;
import com.example.eLibrary.entity.user.User;
import com.example.eLibrary.entity.user.UserRole;
import com.example.eLibrary.payload.request.LoginRequest;
import com.example.eLibrary.payload.request.SignupRequest;
import com.example.eLibrary.payload.response.AuthResponse;
import com.example.eLibrary.repository.user.UserRepository;
import com.example.eLibrary.repository.user.UserRoleRepository;
import com.example.eLibrary.security.jwt.AuthTokenFilter;
import com.example.eLibrary.security.jwt.JwtService;
import com.example.eLibrary.security.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.NoSuchElementException;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRoleRepository roleRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthTokenFilter authTokenFilter;

    public AuthResponse register(SignupRequest request) {
        UserRole userRole = roleRepository.findByUserRole("ROLE_USER");
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .userRole(userRole)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthResponse login(@NotNull LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    private String generateRandomPassword() {

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%^&+=";

        int passwordLength = 9;

        Random random = new SecureRandom();

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < passwordLength; i++) {
            int randomIndex = random.nextInt(characters.length());
            password.append(characters.charAt(randomIndex));
        }

        return password.toString();
    }

    public String getLoggedUserEmail() {
        String loggedUserEmail;
        Object loggedUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (loggedUser instanceof UserDetails) {
            loggedUserEmail = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        } else {
            throw new ClassCastException();
        }
        return loggedUserEmail;
    }

    public void updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        String passwordToVerify = updatePasswordRequest.getCurrentPassword();
        String loggedUserEmail = getLoggedUserEmail();
        User foundUser = userRepository.findByEmail(loggedUserEmail).orElseThrow(() -> new NoSuchElementException(String.format("There is no user registered with email %s.", loggedUserEmail)));
        if (passwordEncoder.matches(passwordToVerify, foundUser.getPassword())) {
            foundUser.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
            userRepository.save(foundUser);
        } else {
            throw new NoSuchElementException("There is no user with the provided password.");
        }
    }
}