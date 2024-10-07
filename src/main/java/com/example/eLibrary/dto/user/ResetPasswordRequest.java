package com.example.eLibrary.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ResetPasswordRequest {
    @NotBlank(message = "Email address is required.")
    private String email;
}
