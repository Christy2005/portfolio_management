package com.portfolio.portfolio_management.controller;

import com.portfolio.portfolio_management.entity.Project;
import com.portfolio.portfolio_management.entity.UserProfile;
import com.portfolio.portfolio_management.repository.ProjectRepository;
import com.portfolio.portfolio_management.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    // Show the project creation form
    @GetMapping("/project/new")
    public String showCreateProjectForm(Model model) {
        Project project = new Project();
        model.addAttribute("project", project);
        return "create_project"; // users are automatically the logged-in user
    }

    // Save project (create or edit)
    @PostMapping("/project/save")
    public String saveProject(@ModelAttribute("project") Project project) {

        // Assign the logged-in user automatically
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        UserProfile user = userProfileRepository.findByEmail(email);
        project.setUser(user);

        projectRepository.save(project);
        return "redirect:/projects";
    }

    // List projects of the logged-in user
    @GetMapping("/projects")
    public String listProjects(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        UserProfile user = userProfileRepository.findByEmail(email);

        model.addAttribute("projects", projectRepository.findByUserId(user.getId()));
        return "projects";
    }

    // Edit project (only accessible to the owner)
    @GetMapping("/project/edit/{id}")
    public String editProject(@PathVariable Long id, Model model) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if(optionalProject.isPresent()) {
            Project project = optionalProject.get();

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            UserProfile user = userProfileRepository.findByEmail(email);

            if(!project.getUser().getId().equals(user.getId())) {
                return "redirect:/projects"; // block others from editing
            }

            model.addAttribute("project", project);
            return "create_project";
        } else {
            return "redirect:/projects";
        }
    }

    // Delete project (only owner can delete)
    @GetMapping("/project/delete/{id}")
    public String deleteProject(@PathVariable Long id) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if(optionalProject.isPresent()) {
            Project project = optionalProject.get();
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            UserProfile user = userProfileRepository.findByEmail(email);

            if(project.getUser().getId().equals(user.getId())) {
                projectRepository.deleteById(id);
            }
        }
        return "redirect:/projects";
    }
}
