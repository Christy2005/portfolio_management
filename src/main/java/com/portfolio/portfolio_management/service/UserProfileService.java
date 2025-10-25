package com.portfolio.portfolio_management.service;

import com.portfolio.portfolio_management.entity.UserProfile;
import com.portfolio.portfolio_management.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProfileService {

    private final UserProfileRepository repo;

    public UserProfileService(UserProfileRepository repo) {
        this.repo = repo;
    }

    public UserProfile saveUser(UserProfile user) {
        return repo.save(user);
    }

    public List<UserProfile> getAllUsers() {
        return repo.findAll();
    }
}
