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
@RequestMapping("/index")
public class IndexController {

    private final AuthenticationService authService;

    public IndexController(AuthenticationService authService) {
        this.authService = authService;
    }

    @GetMapping("/")
    public String showIndex(Model model) {
        return "index";  // Това връща името на Thymeleaf шаблона (login.html)
    }
}
