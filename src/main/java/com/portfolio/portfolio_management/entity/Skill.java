package com.portfolio.portfolio_management.entity;
import jakarta.persistence.*;

@Entity
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;     // e.g. "Java"
    private String level;    // e.g. "Intermediate", "Expert"

    // Relationship with User
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserProfile user;

    // Constructors
    public Skill() {}
    public Skill(String name, String level, UserProfile user) {
        this.name = name;
        this.level = level;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public UserProfile getUser() { return user; }
    public void setUser(UserProfile user) { this.user = user; }
}
