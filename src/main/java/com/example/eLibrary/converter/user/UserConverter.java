package com.example.eLibrary.converter.user;

import com.example.eLibrary.dto.user.UserResponse;
import com.example.eLibrary.entity.user.User;
import com.example.eLibrary.payload.request.SignupRequest;
import com.example.eLibrary.payload.response.UserInfoResponse;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public User toUser(SignupRequest request){
        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }

    public UserInfoResponse toResponse(User savedUser){
        return new UserInfoResponse(
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getUserRole().getUserRole());
    }

    public UserResponse toUserResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .createdDate(user.getCreatedDate())
                .userRole(user.getUserRole())
                .build();
    }
}
