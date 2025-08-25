package com.instagram.service;

import com.instagram.entity.User;
import com.instagram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Optional;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @PostConstruct
    public void seedDemoUser() {
        if (!userRepository.existsByUsername("demo")) {
            User demoUser = new User("demo", passwordEncoder.encode("demo123"));
            userRepository.save(demoUser);
            System.out.println("Demo user created: demo/demo123");
        }
    }
    
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public boolean validateCredentials(String username, String rawPassword) {
        Optional<User> userOpt = findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return passwordEncoder.matches(rawPassword, user.getPasswordHash());
        }
        return false;
    }
    
    public User createUser(String username, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        
        User user = new User(username, passwordEncoder.encode(rawPassword));
        return userRepository.save(user);
    }
}
