package com.example.bayan.Controller;


import com.example.bayan.Api.ApiResponse;
import com.example.bayan.DTO.IN.Post.AddPostDTO;
import com.example.bayan.DTO.OUT.Post.PostDTO;
import com.example.bayan.Model.MyUser;
import com.example.bayan.Service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bayan/post")
@RequiredArgsConstructor
public class PostController {


    private final PostService postService;

    @PostMapping("/add/{userId}")
    public ResponseEntity addPost(@PathVariable Integer userId, @RequestBody @Valid AddPostDTO addPostDTO) {

        postService.addPost(userId,addPostDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("post created successfully"));
    }

    @GetMapping("/my-posts/{userId}")
    public ResponseEntity getMyPosts(@PathVariable Integer userId) {

        List<PostDTO> postDTOList = postService.myPost(userId);

        return ResponseEntity.status(HttpStatus.OK).body(postDTOList);
    }


    @PutMapping("/update/{userId}/{postId}")
    public ResponseEntity updatePost(@PathVariable Integer userId, @PathVariable Integer postId, @RequestBody @Valid AddPostDTO addPostDTO) {

        postService.update(userId, postId, addPostDTO);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Post updated successfully"));
    }


    @DeleteMapping("/delete/{customerId}/{postId}")
    public ResponseEntity deletePost(@PathVariable Integer customerId, @PathVariable Integer postId) {

        postService.deletePost(postId, customerId);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Post deleted successfully"));
    }


}
