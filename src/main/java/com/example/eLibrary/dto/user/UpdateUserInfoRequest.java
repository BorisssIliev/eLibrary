package com.example.eLibrary.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateUserInfoRequest {
    @NotNull
    @Size(min = 2, max = 255, message = "First name length must be between {min} and {max} characters")
    private String firstName;

    @NotNull
    @Size(min = 2, max = 255, message = "Last name length must be between {min} and {max} characters")
    private String lastName;


}
