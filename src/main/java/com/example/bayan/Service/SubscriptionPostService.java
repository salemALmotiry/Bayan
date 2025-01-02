package com.example.bayan.Service;

import com.example.bayan.Api.ApiException;
import com.example.bayan.DTO.IN.Post.AddPostDTO;
import com.example.bayan.DTO.IN.Post.SubscriptionPostDTO;
import com.example.bayan.Model.*;
import com.example.bayan.Repostiry.AuthRepository;
import com.example.bayan.Repostiry.BorderRepository;
import com.example.bayan.Repostiry.CustomerRepository;
import com.example.bayan.Repostiry.SubscriptionPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionPostService {

    private final SubscriptionPostRepository subscriptionPostRepository;
    private final AuthRepository authRepository;
    private final CustomerRepository customerRepository;
    private final BorderRepository borderRepository;


    // create a post for Subscription shipments
    // customer create Subscription Post to request for many shipments
    public void createSubscriptionPost(Integer customer_id, SubscriptionPostDTO subscriptionPost) {
        MyUser user = authRepository.findMyUserById(customer_id);
        if (user == null) {
            throw new ApiException("Wrong user Id");
        }
        Customer customer = customerRepository.findCustomerByUser(user);
        if (customer == null) {
            throw new ApiException("Customer not found for the user.");
        }
        Border border = borderRepository.findBorderByName(subscriptionPost.getBorderName());
        if (border == null) {
            throw new ApiException("No border found with the name: " + subscriptionPost.getBorderName());
        }
        SubscriptionPost post = new SubscriptionPost();
        post.setTitle(post.getTitle());
        post.setShipmentType(post.getShipmentType());
        post.setProductCategory(post.getProductCategory());
        post.setBorder(border);
        post.setCountryOfOrigin(post.getCountryOfOrigin());
        post.setWeight(post.getWeight());
        post.setHasDocuments(post.getHasDocuments());
        post.setHasDelivery(post.getHasDelivery());
        post.setCustomer(customer);
        subscriptionPost.setShipmentsNumber(subscriptionPost.getShipmentsNumber()); // set the Shipments Number for the subscription Post
        subscriptionPostRepository.save(post);
    }


    // get myPost .. subscription Posts by customer
    public List<com.example.bayan.DTO.OUT.Post.SubscriptionPostDTO> mySubscriptionsPosts (Integer customer_id){
        MyUser user = authRepository.findMyUserById(customer_id);
        if (user == null) {
            throw new ApiException("Wrong user Id");
        }
        Customer customer = customerRepository.findCustomerByUser(user);
        if (customer == null) {
            throw new ApiException("Customer not found for the user.");
        }

        List<SubscriptionPost> posts = subscriptionPostRepository.findAllByCustomerId(customer_id);

        List<com.example.bayan.DTO.OUT.Post.SubscriptionPostDTO> subscriptionPostDTOS = new ArrayList<>();

        for (SubscriptionPost post : posts){
            com.example.bayan.DTO.OUT.Post.SubscriptionPostDTO postDTO = new com.example.bayan.DTO.OUT.Post.SubscriptionPostDTO();
            postDTO.setStatus(post.getStatus());
            postDTO.setTitle(post.getTitle());
            postDTO.setProductCategory(post.getProductCategory());
            postDTO.setWeight(post.getWeight());
            postDTO.setShipmentType(post.getShipmentType());
            postDTO.setCountryOfOrigin(post.getCountryOfOrigin());
            postDTO.setStatus(post.getStatus());
            postDTO.setHasDelivery(post.getHasDelivery());
            postDTO.setHasDocuments(post.getHasDocuments());
            postDTO.setShipmentsNumber(post.getShipmentsNumber());

            subscriptionPostDTOS.add(postDTO);
        }
        return subscriptionPostDTOS;
    }

    // update subscription Post
    public void updateSubscriptionPost(Integer customerId, Integer postId,SubscriptionPostDTO subscriptionPostDTO){

        Customer customer = customerRepository.findCustomerById(customerId);

        if (customer == null){
            throw new ApiException("Customer with this"+customerId+" does not exist");
        }

        SubscriptionPost subscriptionPost = subscriptionPostRepository.findPostById(postId);

        if (subscriptionPost == null){
            throw new ApiException("Post with this"+postId+" does not exist");
        }

        if (!subscriptionPost.getStatus().equals("Pending")){
            throw new ApiException("Post with this"+postId+"is not Pending");
        }

        Border border = borderRepository.findBorderByName(subscriptionPost.getBorder().getName());
        if (border == null) {
            throw new ApiException("Border not found with the name: " + subscriptionPostDTO.getBorderName());
        }
        subscriptionPost.setTitle(subscriptionPostDTO.getTitle());
        subscriptionPost.setShipmentType(subscriptionPostDTO.getShipmentType());
        subscriptionPost.setProductCategory(subscriptionPostDTO.getProductCategory());
        subscriptionPost.setBorder(border);
        subscriptionPost.setCountryOfOrigin(subscriptionPostDTO.getCountryOfOrigin());
        subscriptionPost.setWeight(subscriptionPostDTO.getWeight());
        subscriptionPost.setHasDocuments(subscriptionPostDTO.getHasDocuments());
        subscriptionPost.setHasDelivery(subscriptionPostDTO.getHasDelivery());
        subscriptionPost.setCustomer(customer);
        subscriptionPost.setShipmentsNumber(subscriptionPost.getShipmentsNumber()); // set the Shipments Number for the subscription Post
        subscriptionPostRepository.save(subscriptionPost);

    }

    // delete subscription Post
    public void deleteSubscriptionPost(Integer postId,Integer customerId){
        SubscriptionPost post = subscriptionPostRepository.findPostById(postId);
        if(post==null){
            throw new ApiException("Post with this"+postId+" does not exist");
        }

        if (!post.getStatus().equals("Pending")){
            throw new ApiException("Post with this"+postId+"is not Pending");
        }

        Customer customer=customerRepository.findCustomerById(customerId);

        if(customer==null){
            throw new ApiException("Customer with this"+customerId+" does not exist");
        }
        subscriptionPostRepository.delete(post);
    }


}
