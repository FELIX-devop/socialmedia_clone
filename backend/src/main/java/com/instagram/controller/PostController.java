package com.instagram.controller;

import com.instagram.entity.Post;
import com.instagram.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class PostController {
    
    private final PostService postService;
    
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }
    
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        try {
            List<Post> posts = postService.getAllPosts();
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<?> createPost(
            @RequestParam("image") MultipartFile image,
            @RequestParam("caption") String caption,
            @RequestParam("userId") String userId) {
        
        try {
            if (image.isEmpty()) {
                return ResponseEntity.badRequest().body("Image file is required");
            }
            
            Post post = postService.createPost(image, caption, userId);
            return ResponseEntity.ok(post);
            
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Invalid image")) {
                return ResponseEntity.badRequest().body("Invalid image file");
            }
            return ResponseEntity.badRequest().body("Error creating post: " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error processing image: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Unexpected error: " + e.getMessage());
        }
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Post>> getPostsByUserId(@PathVariable String userId) {
        try {
            List<Post> posts = postService.getPostsByUserId(userId);
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
