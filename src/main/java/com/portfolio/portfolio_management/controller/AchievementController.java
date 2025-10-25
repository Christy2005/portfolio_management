package com.portfolio.portfolio_management.controller;

import com.portfolio.portfolio_management.entity.Achievement;
import com.portfolio.portfolio_management.entity.UserProfile;
import com.portfolio.portfolio_management.repository.AchievementRepository;
import com.portfolio.portfolio_management.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/achievements")
public class AchievementController {

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @PostMapping("/add")
    public String addAchievement(@RequestParam String title,
                                 @RequestParam String description,
                                 @RequestParam String year) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        UserProfile user = userProfileRepository.findByEmail(email);

        if (user == null) {
            return "redirect:/login?error=profile-missing";
        }

        Achievement achievement = new Achievement(title, description, year, user);
        achievementRepository.save(achievement);

        return "redirect:/projects"; // Redirect to the consolidated view
    }

    @GetMapping("/delete/{id}")
    public String deleteAchievement(@PathVariable Long id) {
        Optional<Achievement> optionalAchievement = achievementRepository.findById(id);

        if (optionalAchievement.isPresent()) {
            Achievement achievement = optionalAchievement.get();
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            UserProfile user = userProfileRepository.findByEmail(email);

            // Authorization check: Ensure logged-in user owns the achievement
            if (user != null && achievement.getUser() != null && achievement.getUser().getId().equals(user.getId())) {
                achievementRepository.deleteById(id);
            }
        }
        return "redirect:/projects"; // Redirect to the consolidated view
    }
}