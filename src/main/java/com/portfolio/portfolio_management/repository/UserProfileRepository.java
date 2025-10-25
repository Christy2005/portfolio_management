package com.portfolio.portfolio_management.repository;

import com.portfolio.portfolio_management.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findByEmail(String email);
}
