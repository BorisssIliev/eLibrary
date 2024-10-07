package com.example.eLibrary.controller.securityControllers;

import com.example.eLibrary.dto.user.UpdatePasswordRequest;
import com.example.eLibrary.security.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class UpdatePasswordController {
    private final AuthenticationService authService;

    @Autowired
    public UpdatePasswordController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PutMapping("/user/update-password")
    public ResponseEntity<String> updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest){

        authService.updatePassword(updatePasswordRequest);

        return ResponseEntity.ok("Your password has been updated.");
    }

}
