package com.example.eLibrary.controller.user;

import com.example.eLibrary.controller.securityControllers.AuthController;
import com.example.eLibrary.payload.request.LoginRequest;
import com.example.eLibrary.payload.request.SignupRequest;
import com.example.eLibrary.payload.response.AuthResponse;
import com.example.eLibrary.security.AuthenticationService;
import com.example.eLibrary.security.jwt.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authService;

    @MockBean
    private JwtService jwtService;

    @Test
    @WithMockUser
    public void testRegisterUser() throws Exception {
        SignupRequest signupRequest = SignupRequest.builder()
                .firstName("Atanas")
                .lastName("Krastev")
                .email("nasko@gmail.com")
                .password("passWord12@")
                .build();

        AuthResponse authResponse = AuthResponse.builder()
                .token("dummy-token")
                .build();

        when(authService.register(any(SignupRequest.class))).thenReturn(authResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signupRequest))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"token\":\"dummy-token\"}"));
    }

    @Test
    @WithMockUser
    public void testLoginUser_WithCSRF() throws Exception {

        LoginRequest loginRequest = LoginRequest.builder()
                .email("nasko@gmail.com")
                .password("passWord12@")
                .build();

        AuthResponse authResponse = AuthResponse.builder()
                .token("dummy-token")
                .build();

        when(authService.login(any(LoginRequest.class))).thenReturn(authResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"token\":\"dummy-token\"}"));
    }
}