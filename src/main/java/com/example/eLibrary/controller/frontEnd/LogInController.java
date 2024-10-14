package com.example.eLibrary.controller.frontEnd;

import com.example.eLibrary.payload.request.LoginRequest;
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
public class LogInController {

    private final AuthenticationService authService;

    public LogInController(AuthenticationService authService) {
        this.authService = authService;
    }

    // Метод за показване на логин формата (GET заявка)
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";  // Това връща името на Thymeleaf шаблона (login.html)
    }

    // Метод за обработка на логина (POST заявка)
    @PostMapping("/login")
    public String loginUser(@ModelAttribute("loginRequest") @Valid LoginRequest loginRequest,
                            BindingResult bindingResult,
                            Model model) {
        // Проверка за грешки при валидация на данните
        if (bindingResult.hasErrors()) {
            return "login";  // Връщаме същата форма с грешките
        }

        // Логваме потребителя чрез AuthenticationService
        AuthResponse authResponse = authService.login(loginRequest);

        // Предполагаме, че ако authResponse връща някакви данни (например токен или успешен отговор), значи логинът е успешен
        if (authResponse != null && authResponse.getToken() != null) {
            return "redirect:/index";  // Пренасочване към индекс страницата при успешен логин
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "login";  // Връщаме същата форма с грешка
        }
    }
}
