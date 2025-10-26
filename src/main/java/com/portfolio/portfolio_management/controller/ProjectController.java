package com.portfolio.portfolio_management.controller;

import com.portfolio.portfolio_management.entity.Project;
import com.portfolio.portfolio_management.entity.Skill; // Required for skills logic
import com.portfolio.portfolio_management.entity.UserProfile;
import com.portfolio.portfolio_management.repository.ProjectRepository;
import com.portfolio.portfolio_management.repository.UserProfileRepository;
import com.portfolio.portfolio_management.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.portfolio.portfolio_management.repository.AchievementRepository;
import com.portfolio.portfolio_management.entity.Achievement;
import java.util.List;
import java.util.Optional;

@Controller
public class ProjectController {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private ProjectRepository projectRepository;

    // Use a single instance for the UserProfileRepository
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired // NEW INJECTION
    private AchievementRepository achievementRepository;
    // Show the project creation form (Keep as is)
    @GetMapping("/project/new")
    public String showCreateProjectForm(Model model) {
        Project project = new Project();
        model.addAttribute("project", project);
        return "create_project";
    }

    // Save project (create or edit) (Keep as is)
    @PostMapping("/project/save")
    public String saveProject(@ModelAttribute("project") Project project) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        UserProfile user = userProfileRepository.findByEmail(email);

        if (user == null) {
            return "redirect:/login?error=profile-missing";
        }

        project.setUser(user);
        projectRepository.save(project);
        return "redirect:/projects";
    }

    @GetMapping("/projects")
    public String listProjects(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        // 1. Authentication Check
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(email)) {
            return "redirect:/login";
        }

        // 2. Fetch User Profile
        UserProfile user = userProfileRepository.findByEmail(email);


        if (user == null) {
            return "redirect:/login?error=profile-missing";
        }

        Long userId = user.getId();

        List<Project> projects = projectRepository.findByUserId(userId);
        model.addAttribute("projects", projects);


        List<Skill> skills = skillRepository.findByUser(user);
        model.addAttribute("skills", skills);
        List<Achievement> achievements = achievementRepository.findByUser(user);
        model.addAttribute("achievements", achievements); // <-- NEW MODEL ATTRIBUTE
        // 6. Return the template (Must be projects.html)
        return "projects";
    }

    @GetMapping("/project/edit/{id}")
    public String editProject(@PathVariable Long id, Model model) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if(optionalProject.isPresent()) {
            Project project = optionalProject.get();

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            UserProfile user = userProfileRepository.findByEmail(email);

            if(user == null || !project.getUser().getId().equals(user.getId())) { // Enhanced check
                return "redirect:/projects";
            }

            model.addAttribute("project", project);
            return "create_project";
        } else {
            return "redirect:/projects";
        }
    }

    // Delete project (only owner can delete) (Keep as is)
    @GetMapping("/project/delete/{id}")
    public String deleteProject(@PathVariable Long id) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if(optionalProject.isPresent()) {
            Project project = optionalProject.get();
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            UserProfile user = userProfileRepository.findByEmail(email);

            if(user != null && project.getUser().getId().equals(user.getId())) { // Enhanced check
                projectRepository.deleteById(id);
            }
        }
        return "redirect:/projects";
    }
}