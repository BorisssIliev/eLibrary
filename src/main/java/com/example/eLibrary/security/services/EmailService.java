package com.example.eLibrary.security.services;

import com.example.eLibrary.entity.user.User;

public interface EmailService {
    void sendPasswordResetEmail(User user, String newPassword);
}
