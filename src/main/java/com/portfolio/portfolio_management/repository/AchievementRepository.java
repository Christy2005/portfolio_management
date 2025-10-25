package com.portfolio.portfolio_management.repository;

import com.portfolio.portfolio_management.entity.Achievement;
import com.portfolio.portfolio_management.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {

    // Derived query to find achievements by the associated UserProfile object
    List<Achievement> findByUser(UserProfile user);
}