package com.instagram.repository;

import com.instagram.entity.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    
    @Query(value = "{}", sort = "{'createdAt': -1}")
    List<Post> findAllOrderByCreatedAtDesc();
    
    List<Post> findByUserIdOrderByCreatedAtDesc(String userId);
}
