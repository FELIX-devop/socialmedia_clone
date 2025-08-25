package com.instagram.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ImageValidationClient {
    
    @Value("${python.validator.url:http://localhost:5001}")
    private String validatorUrl;
    
    private final RestTemplate restTemplate;
    
    public ImageValidationClient() {
        this.restTemplate = new RestTemplate();
    }
    
    public boolean validateImage(MultipartFile file) {
        try {
            // Prepare the request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            
            // Create multipart request body
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };
            body.add("file", fileResource);
            
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            
            // Make request to Python validator
            ResponseEntity<Map> response = restTemplate.postForEntity(
                validatorUrl + "/validate",
                requestEntity,
                Map.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null && Boolean.TRUE.equals(responseBody.get("ok"))) {
                    return true;
                }
            }
            
            return false;
            
        } catch (IOException e) {
            // Log the error
            System.err.println("Error reading file for validation: " + e.getMessage());
            return false;
        } catch (Exception e) {
            // Log the error
            System.err.println("Error during image validation: " + e.getMessage());
            return false;
        }
    }
}
