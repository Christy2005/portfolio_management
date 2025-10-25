package com.portfolio.portfolio_management.controller;

import com.portfolio.portfolio_management.entity.UserProfile;
import com.portfolio.portfolio_management.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Show registration form
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserProfile());
        return "register";
    }

    // Handle registration
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserProfile user) {
        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userProfileRepository.save(user);
        return "redirect:/login";
    }

    // Show login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
}
