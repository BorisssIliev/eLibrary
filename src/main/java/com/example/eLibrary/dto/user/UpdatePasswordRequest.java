package com.example.eLibrary.dto.user;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdatePasswordRequest {
    @NotNull
    @NotBlank(message = "Password field may not be blank")
    @Size(min = 8, max = 255, message = "Password must be between {min} and {max} characters")
    @Pattern(regexp = "^.*(?=.{8,})(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", message = "Incorrect password pattern. Password must be at least 8 characters and include at least one digit, one lowercase letter, one uppercase letter and one special character(\"@#$%^&+=\").")
    private String currentPassword;

    @NotNull
    @NotBlank(message = "Password field may not be blank")
    @Size(min = 8, max = 255, message = "Password must be between {min} and {max} characters")
    @Pattern(regexp = "^.*(?=.{8,})(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", message = "Incorrect password pattern. Password must be at least 8 characters and include at least one digit, one lowercase letter, one uppercase letter and one special character(\"@#$%^&+=\").")
    private String newPassword;
}
