package com.instagram.controller;

import com.instagram.dto.LoginRequest;
import com.instagram.dto.LoginResponse;
import com.instagram.entity.User;
import com.instagram.service.JwtService;
import com.instagram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class AuthController {
    
    private final UserService userService;
    private final JwtService jwtService;
    
    @Autowired
    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            if (userService.validateCredentials(loginRequest.getUsername(), loginRequest.getPassword())) {
                Optional<User> userOpt = userService.findByUsername(loginRequest.getUsername());
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    String token = jwtService.generateToken(user.getUsername());
                    
                    LoginResponse response = new LoginResponse(
                        user.getId(),
                        user.getUsername(),
                        token
                    );
                    
                    return ResponseEntity.ok(response);
                }
            }
            
            return ResponseEntity.badRequest().body("Invalid credentials");
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Login failed: " + e.getMessage());
        }
    }
}
