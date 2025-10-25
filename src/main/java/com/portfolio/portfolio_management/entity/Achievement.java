package com.portfolio.portfolio_management.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "achievements")
public class Achievement {

    public UserProfile getUser() {
        return user;
    }

    public void setUser(UserProfile user) {
        this.user = user;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String year;

    // Link to the UserProfile
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserProfile user;

    // Constructors
    public Achievement() {}

    public Achievement(String title, String description, String year, UserProfile user) {
        this.title = title;
        this.description = description;
        this.year = year;
        this.user = user;
    }


}