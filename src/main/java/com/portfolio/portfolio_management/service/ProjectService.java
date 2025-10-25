package com.portfolio.portfolio_management.service;

import com.portfolio.portfolio_management.entity.Project;
import com.portfolio.portfolio_management.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository repo;

    public ProjectService(ProjectRepository repo) {
        this.repo = repo;
    }

    public Project saveProject(Project project) {
        return repo.save(project);
    }

    public List<Project> getProjectsByUserId(Long userId) {
        return repo.findByUserId(userId);
    }

    public List<Project> getAllProjects() {
        return repo.findAll();
    }
}
