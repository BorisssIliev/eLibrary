package com.example.eLibrary.controller.frontEnd;

import com.example.eLibrary.payload.request.LoginRequest;
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

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());  // Добавяме празен LoginRequest обект към модела
        return "login";  // Това връща името на Thymeleaf шаблона (login.html)
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("loginRequest") @Valid LoginRequest loginRequest,
                            BindingResult bindingResult,
                            Model model) {
        // Проверка за грешки при валидация на данните
        if (bindingResult.hasErrors()) {
            return "login";  // Връщаме същата форма с грешките
        }

        // Тук добавете логиката за автентикация на потребителя
        // Например може да използвате AuthenticationManager за логин

        // Ако автентикацията е успешна:
        return "redirect:/user/dashboard";  // Пренасочване след успешен логин
    }
}
