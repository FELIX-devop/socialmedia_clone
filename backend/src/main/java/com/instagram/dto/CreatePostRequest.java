package com.instagram.dto;

import org.springframework.web.multipart.MultipartFile;

public class CreatePostRequest {
    private MultipartFile image;
    private String caption;
    private String userId;
    
    public CreatePostRequest() {}
    
    public MultipartFile getImage() {
        return image;
    }
    
    public void setImage(MultipartFile image) {
        this.image = image;
    }
    
    public String getCaption() {
        return caption;
    }
    
    public void setCaption(String caption) {
        this.caption = caption;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
