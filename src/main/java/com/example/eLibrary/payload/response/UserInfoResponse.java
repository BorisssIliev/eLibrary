package com.example.eLibrary.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserInfoResponse {

    private String firstName;
    private String lastName;
    private String email;
    private String userRole;

}
