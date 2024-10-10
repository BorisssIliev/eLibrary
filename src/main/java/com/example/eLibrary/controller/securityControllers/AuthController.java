package com.example.eLibrary.controller.securityControllers;

import com.example.eLibrary.payload.request.LoginRequest;
import com.example.eLibrary.payload.request.SignupRequest;
import com.example.eLibrary.payload.response.AuthResponse;
import com.example.eLibrary.security.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationService authService;


    @PostMapping(path = "/register")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody SignupRequest request) {

        return ResponseEntity.ok(authService.register(request));
    }


    @PostMapping("/login")
    @Operation(
            description = "Log in a user",
            summary = "User Login",
            responses = {
                    @ApiResponse(
                            description = "User logged in successfully",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthResponse.class)
                            )
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User login request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginRequest.class),
                            examples = @ExampleObject(value =
                                    "{" +
                                    "  \"email\": \"nasko@gmail.com\"," +
                                    "  \"password\": \"passWord12@\"" +
                                    "}"
                            )
                    )
            ),
            operationId = "loginUser",
            tags = {"User Authentication"},
            security = @SecurityRequirement(name = "Bearer Token")
    )
    public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginRequest loginRequest) {

      return ResponseEntity.ok(authService.login(loginRequest));

    }

}
