package com.example.eLibrary.dto.savedBook;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Data
public class SavedBookRequestDto {

    @NotNull
    private Long bookId;

}
