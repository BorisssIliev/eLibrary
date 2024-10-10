package com.example.eLibrary.controller.securityControllers.frontEnd;

import com.example.eLibrary.payload.request.SignupRequest;
import com.example.eLibrary.security.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class RegistrationController {

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register"; // Тук се връща името на шаблона без .html
    }

    // Добави метода за обработка на POST заявките за регистрация
    @PostMapping("/register")
    public String registerUser(@ModelAttribute SignupRequest signupRequest) {
        // Логиката за регистрация на потребителя
        return "redirect:/auth/login"; // Пренасочване след успешна регистрация
    }
}
