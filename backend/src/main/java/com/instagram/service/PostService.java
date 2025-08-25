package com.instagram.service;

import com.instagram.entity.Post;
import com.instagram.entity.User;
import com.instagram.repository.PostRepository;
import com.instagram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostService {
    
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageValidationClient imageValidationClient;
    
    private static final String UPLOAD_DIR = "uploads";
    
    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, ImageValidationClient imageValidationClient) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.imageValidationClient = imageValidationClient;
        
        // Create uploads directory if it doesn't exist
        createUploadsDirectory();
    }
    
    private void createUploadsDirectory() {
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                System.out.println("Created uploads directory: " + uploadPath.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Error creating uploads directory: " + e.getMessage());
        }
    }
    
    public List<Post> getAllPosts() {
        return postRepository.findAllOrderByCreatedAtDesc();
    }
    
    public Post createPost(MultipartFile image, String caption, String userId) throws IOException {
        // Validate image through Python microservice
        if (!imageValidationClient.validateImage(image)) {
            throw new RuntimeException("Invalid image file");
        }
        
        // Get user info
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        
        User user = userOpt.get();
        
        // Save image to disk
        String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR, fileName);
        Files.copy(image.getInputStream(), filePath);
        
        // Create post
        Post post = new Post("/uploads/" + fileName, caption, userId, user.getUsername());
        return postRepository.save(post);
    }
    
    public List<Post> getPostsByUserId(String userId) {
        return postRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}
