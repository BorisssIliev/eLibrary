package com.example.eLibrary.controller.frontEnd;

import com.example.eLibrary.payload.request.SignupRequest;
import com.example.eLibrary.payload.response.AuthResponse;
import com.example.eLibrary.security.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class RegistrationController {

    private final AuthenticationService authService;

    public RegistrationController(AuthenticationService authService) {
        this.authService = authService;
    }

    // Метод за показване на регистрационната форма (GET заявка)
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("signupRequest", new SignupRequest());
        return "register";  // Това връща името на Thymeleaf шаблона (register.html)
    }

    // Метод за обработка на регистрацията (POST заявка)
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("signupRequest") @Valid SignupRequest signupRequest,
                               BindingResult bindingResult,
                               Model model) {
        // Проверка за грешки при валидация на данните
        if (bindingResult.hasErrors()) {
            return "register";  // Връщаме същата форма с грешките
        }

        // Регистрираме потребителя чрез AuthenticationService
        AuthResponse authResponse = authService.register(signupRequest);

        // Добавяме успешно съобщение или правим redirect
        model.addAttribute("message", "Registration successful!");

        return "redirect:/auth/login";  // Пренасочване към логин страницата след успешна регистрация
    }
}

