package com.example.eLibrary.service.user.impl;

import com.example.eLibrary.entity.user.UserRole;
import com.example.eLibrary.repository.user.UserRoleRepository;
import com.example.eLibrary.service.user.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserRole createUserRole(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }


    @Override
    public Optional<UserRole> getUserRoleById(Long id) {
        return userRoleRepository.findById(id);
    }


    @Override
    public UserRole updateUserRole(Long userRoleId, UserRole updatedUserRole) {
        UserRole extantUserRole = userRoleRepository.findById(userRoleId).orElse(null);

        if (extantUserRole != null) {
            extantUserRole.setUserRole(updatedUserRole.getUserRole());
        }
        return userRoleRepository.save(updatedUserRole);
    }

    @Override
    public void deleteUserRole(Long userRoleId) {

    }

    @Override
    public UserRole findByUserRoleName(String roleName) {
        return userRoleRepository.findByUserRole(roleName);
    }
}
