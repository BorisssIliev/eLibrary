package com.example.eLibrary.service.user;

import com.example.eLibrary.entity.user.UserRole;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserRoleService {
    UserRole createUserRole(UserRole userRole);

    Optional<UserRole> getUserRoleById(Long id);

    UserRole updateUserRole(Long userRoleId, UserRole updatedUserRole);

    void deleteUserRole(Long userRoleId);

    UserRole findByUserRoleName(String roleName);
}