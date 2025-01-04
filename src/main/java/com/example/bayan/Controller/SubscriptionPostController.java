package com.example.bayan.Controller;

import com.example.bayan.Api.ApiResponse;
import com.example.bayan.DTO.IN.CustomsBrokerDTO;
import com.example.bayan.DTO.IN.Post.SubscriptionPostDTO;
import com.example.bayan.Model.SubscriptionPost;
import com.example.bayan.Service.SubscriptionPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bayan/subscription-post")
@RequiredArgsConstructor
public class SubscriptionPostController {

    private final SubscriptionPostService subscriptionPostService;

    // add Subscription Post
    @PostMapping("/create-subscription-post/{customer_id}")
    public ResponseEntity<?> createSubscriptionPost(@PathVariable  Integer customer_id ,@RequestBody @Valid SubscriptionPostDTO subscriptionPost){
        subscriptionPostService.createSubscriptionPost(customer_id,subscriptionPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Post created Successfully"));
    }

    // get .. myPosts
    @GetMapping("/my-profile/{customer_id}")
    public ResponseEntity<?> mySubscriptionsPosts(@PathVariable Integer customer_id){
        return ResponseEntity.status(200).body(subscriptionPostService.mySubscriptionsPosts(customer_id));
    }

    // update
    @PutMapping("/update-my-account/{customer_id}/{post_id}")
    public ResponseEntity<?> updateSubscriptionPost(@PathVariable Integer customer_id ,@PathVariable Integer post_id , @RequestBody @Valid SubscriptionPostDTO subscriptionPost){
        subscriptionPostService.updateSubscriptionPost(customer_id,post_id,subscriptionPost);
        return ResponseEntity.status(200).body(new ApiResponse("Subscription Post updated Successfully"));
    }

   // delete
    @DeleteMapping("/delete-my-account/{postId}/{customerId}")
    public ResponseEntity<?> deleteSubscriptionPost(@PathVariable Integer postId,@PathVariable Integer customerId){
        subscriptionPostService.deleteSubscriptionPost(postId,customerId);
        return ResponseEntity.status(200).body(new ApiResponse("Subscription Post deleted Successfully"));
    }



}
