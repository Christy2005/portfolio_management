package com.portfolio.portfolio_management.controller;

import com.portfolio.portfolio_management.entity.UserProfile;
import com.portfolio.portfolio_management.service.UserProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserProfileController {

    private final UserProfileService service;

    public UserProfileController(UserProfileService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", service.getAllUsers());
        return "users"; // Thymeleaf template
    }

    @GetMapping("/user/new")
    public String createUserForm(Model model) {
        model.addAttribute("userProfile", new UserProfile());
        return "create_user"; // Form template
    }

    @PostMapping("/user/save")
    public String saveUser(@ModelAttribute UserProfile userProfile) {
        service.saveUser(userProfile);
        return "redirect:/users";
    }
}
