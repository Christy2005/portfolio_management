package com.portfolio.portfolio_management.controller;

import com.portfolio.portfolio_management.entity.Skill;
import com.portfolio.portfolio_management.entity.UserProfile;
import com.portfolio.portfolio_management.repository.SkillRepository;
import com.portfolio.portfolio_management.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/skills")
public class SkillController {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private UserProfileRepository userRepository;

    // List skills of logged-in user
   /* @GetMapping
    public String listSkills(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // logged-in user's email

        UserProfile user = userRepository.findByEmail(email);
        List<Skill> skills = skillRepository.findByUser(user);

        model.addAttribute("skills", skills);
        return "skills"; // Thymeleaf template
    }*/

    // Add skill
    @PostMapping("/add")
    public String addSkill(@RequestParam String name,
                           @RequestParam String level) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        UserProfile user = userRepository.findByEmail(email);
        Skill skill = new Skill(name, level, user);
        skillRepository.save(skill);

        return "redirect:/projects";
    }

    // Delete skill
    @GetMapping("/delete/{id}")
    public String deleteSkill(@PathVariable Long id) {
        skillRepository.deleteById(id);
        return "redirect:/projects";
    }
}
