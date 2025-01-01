package com.example.bayan.Repostiry;

import com.example.bayan.Model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {


    List<Post> findAllByCustomerId(Integer userId);

    Post findPostById(Integer postId);

}
