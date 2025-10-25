package com.portfolio.portfolio_management.repository;

import com.portfolio.portfolio_management.entity.Skill;
import com.portfolio.portfolio_management.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findByUser(UserProfile user);
}