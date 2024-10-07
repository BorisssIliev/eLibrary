package com.example.eLibrary.runner;

import com.example.eLibrary.entity.user.User;
import com.example.eLibrary.entity.user.UserRole;
import com.example.eLibrary.repository.user.UserRepository;
import com.example.eLibrary.repository.user.UserRoleRepository;
import com.example.eLibrary.service.user.UserRoleService;
import com.example.eLibrary.service.user.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@Setter
@Component
public class UserRoleCommandLineRunner implements CommandLineRunner {

    private static final String ADMIN_ROLE_NAME = "ROLE_ADMIN";
    private static final String USER_ROLE_NAME = "ROLE_USER";
    private static final String ADMIN_EMAIL = "admin@example.com";


    private final UserService userService;
    private final UserRoleService userRoleService;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    //I've used constructor injection for dependencies, making the code more readable and testable.
    @Autowired
    public UserRoleCommandLineRunner(
            UserService userService,
            UserRoleService userRoleService,
            UserRepository userRepository,
            UserRoleRepository userRoleRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if "ROLE_ADMIN" role exists
        UserRole adminUserRole = userRoleService.findByUserRoleName(ADMIN_ROLE_NAME);
        if (adminUserRole == null) {
            adminUserRole = UserRole
                    .builder()
                    .userRole(ADMIN_ROLE_NAME)
                    .build();
            userRoleService.createUserRole(adminUserRole);
        }

        // Check if "ROLE_USER" role exists
        UserRole userUserRole = userRoleService.findByUserRoleName(USER_ROLE_NAME);
        if (userUserRole == null) {
            userUserRole = UserRole.builder()
                    .userRole(USER_ROLE_NAME)
                    .build();
            userRoleService.createUserRole(userUserRole);
        }

        //  Check if admin user with email "admin@example.com" exists
        Optional<User> foundUser = userRepository.findByEmail(ADMIN_EMAIL);

        if (foundUser.isEmpty()) {
            User adminUser = User.builder()
                    .firstName("Admin")
                    .lastName("User")
                    .email(ADMIN_EMAIL)
                    .password(bCryptPasswordEncoder.encode(ADMIN_EMAIL))
                    .createdDate(LocalDate.now())
                    .userRole(adminUserRole)
                    .build();
            userRepository.save(adminUser);
        }

    }
}
