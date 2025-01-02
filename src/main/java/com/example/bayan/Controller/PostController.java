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


    @PostMapping("/send-post/{customerId}/{brokerId}")
    public ResponseEntity sendPostForOneBroker(@PathVariable Integer customerId, @PathVariable Integer brokerId, @RequestBody @Valid AddPostDTO addPostDTO) {

        postService.sendPostForOnoBroker(customerId, brokerId, addPostDTO);

        return ResponseEntity.status(200).body(new ApiResponse("Post sent to broker successfully"));
    }

    // Display posts that were sent specifically to the customer

    @GetMapping("/broker/{brokerId}/posts")
    public ResponseEntity<List<PostDTO>> getPostsForBroker(@PathVariable Integer brokerId) {
        List<PostDTO> posts = postService.getPostsForBroker(brokerId);
        return ResponseEntity.status(200).body(posts);
    }

    // Get all posts by category
    @GetMapping("/by-category/{category}")
    public ResponseEntity<?> getAllPostsByCategory(@PathVariable String category) {
        List<Post> posts = postService.getAllPostByCategory(category);
        return ResponseEntity.ok(posts);
    }

    // Get all posts by country of origin
    @GetMapping("/by-country/{countryOfOrigin}")
    public ResponseEntity<?> getAllPostsByCountryOfOrigin(@PathVariable String countryOfOrigin) {
        List<Post> posts = postService.getAllPostByCountryOfOrigin(countryOfOrigin);
        return ResponseEntity.ok(posts);
    }

    // Get all posts by shipment type
    @GetMapping("/by-shipment-type/{shipmentType}")
    public ResponseEntity<?> getAllPostsByShipmentType(@PathVariable String shipmentType) {
        List<Post> posts = postService.getAllPostByShipmentType(shipmentType);
        return ResponseEntity.ok(posts);
    }

    // Get all posts by category and country of origin
    @GetMapping("/by-category-and-country/{category}/{countryOfOrigin}")
    public ResponseEntity<?> getAllPostsByCategoryAndCountryOfOrigin(
            @PathVariable String category,
            @PathVariable String countryOfOrigin) {
        List<Post> posts = postService.getAllPostByTheCategoryAndCountryOfOrigin(category, countryOfOrigin);
        return ResponseEntity.ok(posts);
    }

    // Get all posts by category and shipment type
    @GetMapping("/by-category-and-shipment-type/{category}/{shipmentType}")
    public ResponseEntity<?> getAllPostsByCategoryAndShipmentType(
            @PathVariable String category,
            @PathVariable String shipmentType) {
        List<Post> posts = postService.getAllPostByCategoryAndShipmentType(category, shipmentType);
        return ResponseEntity.ok(posts);
    }

    // Get all posts by category, shipment type, and country of origin
    @GetMapping("/by-category-shipment-type-country/{category}/{shipmentType}/{countryOfOrigin}")
    public ResponseEntity<?> getAllPostsByCategoryAndShipmentTypeAndCountryOfOrigin(
            @PathVariable String category,
            @PathVariable String shipmentType,
            @PathVariable String countryOfOrigin) {
        List<Post> posts = postService.getAllPostByCategoryAndShipmentTypeAndCountryOfOrigin(category, shipmentType, countryOfOrigin);
        return ResponseEntity.ok(posts);
    }
}
