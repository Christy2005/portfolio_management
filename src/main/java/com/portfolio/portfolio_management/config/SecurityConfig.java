package com.portfolio.portfolio_management.config;

import com.portfolio.portfolio_management.repository.UserProfileRepository;
import com.portfolio.portfolio_management.entity.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserProfileRepository userProfileRepository;

    // The password encoder remains correct
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // UPDATED: Simplifies the UserDetails build and ensures the email is used as the principal (username)
    @Bean
    public UserDetailsService userDetailsService() {
        return email -> { // Renamed the parameter to 'email' for clarity, matching the lookup method
            // The UserDetailsService parameter is the 'username' provided at login, which you use as the email.
            UserProfile user = userProfileRepository.findByEmail(email);

            if(user == null) {
                // Throws exception if the user is not found
                throw new UsernameNotFoundException("User not found with email: " + email);
            }

            // Build the Spring Security UserDetails object
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())         // The principal used for Authentication.getName()
                    .password(user.getPassword())       // The stored, BCRYPT-encoded password
                    .roles("USER")                      // Assign a default role
                    .build();
        };
    }

    // The Authentication Provider remains correct
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // The Security Filter Chain remains correct
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        // Public access for registration, login, and static assets
                        .requestMatchers("/register", "/login", "/css/**", "/js/**").permitAll()
                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        // Redirects authenticated users to the /projects page
                        .defaultSuccessUrl("/projects", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll()
                );
        return http.build();
    }
}