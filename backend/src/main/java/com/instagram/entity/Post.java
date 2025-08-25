package com.instagram.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "posts")
public class Post {
    
    @Id
    private String id;
    
    private String imageUrl;
    private String caption;
    private String userId;
    private String username;
    private LocalDateTime createdAt;
    
    public Post() {}
    
    public Post(String imageUrl, String caption, String userId, String username) {
        this.imageUrl = imageUrl;
        this.caption = caption;
        this.userId = userId;
        this.username = username;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters
    public String getId() {
        return id;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public String getCaption() {
        return caption;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    // Setters
    public void setId(String id) {
        this.id = id;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public void setCaption(String caption) {
        this.caption = caption;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
