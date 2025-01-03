package com.example.bayan.Service;


import com.example.bayan.Api.ApiException;
import com.example.bayan.DTO.IN.Post.AddPostDTO;
import com.example.bayan.DTO.OUT.Post.PostDTO;
import com.example.bayan.Model.*;
import com.example.bayan.Repostiry.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CustomerRepository customerRepository;
    private final BorderRepository borderRepository;
    private final AuthRepository authRepository;
    private final CustomBrokerRepository customBrokerRepository;


    public void addPost(Integer userId, AddPostDTO addPostDTO) {
        MyUser myUser = authRepository.findMyUserById(userId);
        if (myUser == null) {
            throw new ApiException("User not found");
        }

        Customer customer = customerRepository.findCustomerByUser(myUser);
        if (customer == null) {
            throw new ApiException("Customer profile not found for the user.");
        }

        Border border = borderRepository.findBorderByName(addPostDTO.getBorderName());
        if (border == null) {
            throw new ApiException("Border not found with the name: " + addPostDTO.getBorderName());
        }

        // Create a new Post entity
        Post post = new Post();
        post.setTitle(addPostDTO.getTitle());
        post.setCategory(addPostDTO.getCategory());
        post.setShipmentType(addPostDTO.getShipmentType());
        post.setCountryOfOrigin(addPostDTO.getCountryOfOrigin());
        post.setWeight(addPostDTO.getWeight());
        post.setHasDocuments(addPostDTO.getHasDocuments());
        post.setHasDelivery(addPostDTO.getHasDelivery());
        post.setBillOfLading(addPostDTO.getBillOfLading());
        post.setCustomer(customer); // Set the customer
        post.setBorder(border);     // Set the border
        postRepository.save(post);
    }

    public List<PostDTO> myPost(Integer userId) {

        MyUser myUser = authRepository.findMyUserById(userId);
        if (myUser == null) {
            throw new ApiException("User not found");
        }
        Customer customer = customerRepository.findCustomerByUser(myUser);

        if (customer == null) {
            throw new ApiException("Customer not found");
        }

        // Fetch posts associated with the customer
        List<Post> posts = postRepository.findAllByCustomerId(customer.getId());

        // Prepare the list of PostDTO
        List<PostDTO> postDTOList = new ArrayList<>();

        for (Post post : posts) {
            PostDTO dto = new PostDTO();
            dto.setTitle(post.getTitle());
            dto.setCategory(post.getCategory());
            dto.setWeight(post.getWeight());
            dto.setShipmentType(post.getShipmentType());
            dto.setCountryOfOrigin(post.getCountryOfOrigin());
            dto.setStatus(post.getStatus());
            dto.setHasDelivery(post.getHasDelivery());
            dto.setHasDocuments(post.getHasDocuments());

            postDTOList.add(dto);
        }

        return postDTOList;
    }

    public void update(Integer userId,Integer postId,AddPostDTO addPostDTO){

        Customer customer = customerRepository.findCustomerById(userId);

        if (customer == null){
            throw new ApiException("Customer with this"+userId+" does not exist");

        }

        Post post = postRepository.findPostById(postId);

        if (post == null) {
            throw new ApiException("Post with this"+postId+" does not exist");

        }
        if (!post.getStatus().equals("Pending")){
            throw new ApiException("Post with this"+postId+"is not Pending");
        }

        Border border = borderRepository.findBorderByName(addPostDTO.getBorderName());
        if (border == null) {
            throw new ApiException("Border not found with the name: " + addPostDTO.getBorderName());
        }

        post.setTitle(addPostDTO.getTitle());
        post.setCategory(addPostDTO.getCategory());
        post.setShipmentType(addPostDTO.getShipmentType());
        post.setCountryOfOrigin(addPostDTO.getCountryOfOrigin());
        post.setWeight(addPostDTO.getWeight());
        post.setHasDocuments(addPostDTO.getHasDocuments());
        post.setHasDelivery(addPostDTO.getHasDelivery());
        post.setBillOfLading(addPostDTO.getBillOfLading());
        post.setCustomer(customer); // Set the customer
        post.setBorder(border);     // Set the border
        postRepository.save(post);


    }

    public void deletePost(Integer postId,Integer customerId){
    Post post = postRepository.findPostById(postId);

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
    postRepository.delete(post);
    }



    // broker
    public void sendPostForOnoBroker(Integer customerId,Integer brokerId, AddPostDTO addPostDTO) {
        // Validate the broker
        CustomsBroker broker = customBrokerRepository.findCustomsBrokerById(brokerId);

        MyUser customer = authRepository.findMyUserById(customerId);


        if (customer == null){
            throw new ApiException("Customer with this"+customerId+" does not exist");
        }
        if(broker==null){
            throw new ApiException("Broker with ID " + brokerId + " not found.");


        }

        Border border = borderRepository.findBorderByName(addPostDTO.getBorderName());
        if (border == null) {
            throw new ApiException("Border not found with the name: " + addPostDTO.getBorderName());
        }

        // Create a new Post entity
        Post post = new Post();
        post.setTitle(addPostDTO.getTitle());
        post.setCategory(addPostDTO.getCategory());
        post.setShipmentType(addPostDTO.getShipmentType());
        post.setCountryOfOrigin(addPostDTO.getCountryOfOrigin());
        post.setWeight(addPostDTO.getWeight());
        post.setHasDocuments(addPostDTO.getHasDocuments());
        post.setHasDelivery(addPostDTO.getHasDelivery());
        post.setBillOfLading(addPostDTO.getBillOfLading());
        post.setBorder(border);

        post.setCustomer(customer.getCustomer());
        post.setCustomsBrokers(broker);

        // Save the new post
        postRepository.save(post);
    }


    public List<PostDTO> getPostsForBroker(Integer brokerId) {
        // Validate the broker
        CustomsBroker broker = customBrokerRepository.findCustomsBrokerById(brokerId);
        if (broker == null) {
            throw new ApiException("Broker with ID " + brokerId + " not found.");
        }

        // Fetch all posts associated with the broker
        List<Post> posts = postRepository.findPostsByCustomsBrokers(broker);

        if (posts.isEmpty()) {
            throw new ApiException("No posts found for the broker with ID " + brokerId);
        }

        // Map Post entities to PostDTO objects
        List<PostDTO> postDTOs = new ArrayList<>();
        for (Post post : posts) {
            PostDTO dto = new PostDTO();
            dto.setTitle(post.getTitle());
            dto.setCategory(post.getCategory());
            dto.setWeight(post.getWeight());
            dto.setShipmentType(post.getShipmentType());
            dto.setCountryOfOrigin(post.getCountryOfOrigin());
            dto.setStatus(post.getStatus());
            dto.setHasDelivery(post.getHasDelivery());
            dto.setHasDocuments(post.getHasDocuments());
            postDTOs.add(dto);
        }

        return postDTOs;
    }

    //****** 6 filter for one page and any one can see it


    // **All Posts By Category (Excluding Posts with a Broker)
    public List<Post> getAllPostByCategory(String category) {
        List<Post> posts = postRepository.findPostByCategory(category);

        if (posts.isEmpty()) {
            throw new ApiException("No posts found for the category " + category);
        }

        // Filter out posts that have a broker assigned
        List<Post> filteredPosts = posts.stream()
                .filter(post -> post.getCustomsBrokers() == null)
                .collect(Collectors.toList());

        if (filteredPosts.isEmpty()) {
            throw new ApiException("No posts without brokers found for the category " + category);
        }

        return filteredPosts;
    }




    // **All Posts By Country Of Origin (Excluding Posts with a Broker)
    public List<Post> getAllPostByCountryOfOrigin(String countryOfOrigin) {
        List<Post> posts = postRepository.findPostByCountryOfOrigin(countryOfOrigin);

        if (posts.isEmpty()) {
            throw new ApiException("No posts found for the specified country of origin: " + countryOfOrigin);
        }

        // Filter out posts that have a broker assigned
        List<Post> filteredPosts = posts.stream()
                .filter(post -> post.getCustomsBrokers() == null)
                .collect(Collectors.toList());

        if (filteredPosts.isEmpty()) {
            throw new ApiException("No posts without brokers found for the specified country of origin: " + countryOfOrigin);
        }

        return filteredPosts;
    }

    // **All Posts By Shipment Type (Excluding Posts with a Broker)
    public List<Post> getAllPostByShipmentType(String shipmentType) {
        List<Post> posts = postRepository.findPostByShipmentType(shipmentType);

        if (posts.isEmpty()) {
            throw new ApiException("No posts found for the specified shipment type: " + shipmentType);
        }

        // Filter out posts that have a broker assigned
        List<Post> filteredPosts = posts.stream()
                .filter(post -> post.getCustomsBrokers() == null)
                .collect(Collectors.toList());

        if (filteredPosts.isEmpty()) {
            throw new ApiException("No posts without brokers found for the specified shipment type: " + shipmentType);
        }

        return filteredPosts;
    }

    // **All Posts By Category And Country Of Origin (Excluding Posts with a Broker)
    public List<Post> getAllPostByTheCategoryAndCountryOfOrigin(String category, String countryOfOrigin) {
        List<Post> posts = postRepository.findPostByCategoryAndCountryOfOrigin(category, countryOfOrigin);

        if (posts.isEmpty()) {
            throw new ApiException("No posts found for the specified category: " + category + " or country of origin: " + countryOfOrigin);
        }

        // Filter out posts that have a broker assigned
        List<Post> filteredPosts = posts.stream()
                .filter(post -> post.getCustomsBrokers() == null)
                .collect(Collectors.toList());

        if (filteredPosts.isEmpty()) {
            throw new ApiException("No posts without brokers found for the specified category: " + category + " and country of origin: " + countryOfOrigin);
        }

        return filteredPosts;
    }

    // **All Posts By Category And Shipment Type (Excluding Posts with a Broker)
    public List<Post> getAllPostByCategoryAndShipmentType(String category, String shipmentType) {
        List<Post> posts = postRepository.findPostByCategoryAndShipmentType(category, shipmentType);

        if (posts.isEmpty()) {
            throw new ApiException("No posts found for the specified category: " + category + " or shipment type: " + shipmentType);
        }

        // Filter out posts that have a broker assigned
        List<Post> filteredPosts = posts.stream()
                .filter(post -> post.getCustomsBrokers() == null)
                .collect(Collectors.toList());

        if (filteredPosts.isEmpty()) {
            throw new ApiException("No posts without brokers found for the specified category: " + category + " and shipment type: " + shipmentType);
        }

        return filteredPosts;
    }

    // **All Posts By Category, Shipment Type, And Country Of Origin (Excluding Posts with a Broker)
    public List<Post> getAllPostByCategoryAndShipmentTypeAndCountryOfOrigin(String category, String shipmentType, String countryOfOrigin) {
        List<Post> posts = postRepository.findPostByCategoryAndShipmentTypeAndCountryOfOrigin(category, shipmentType, countryOfOrigin);

        if (posts.isEmpty()) {
            throw new ApiException("No posts found for the specified category: " + category + ", shipment type: " + shipmentType + ", or country of origin: " + countryOfOrigin);
        }

        // Filter out posts that have a broker assigned
        List<Post> filteredPosts = posts.stream()
                .filter(post -> post.getCustomsBrokers() == null)
                .collect(Collectors.toList());

        if (filteredPosts.isEmpty()) {
            throw new ApiException("No posts without brokers found for the specified category: " + category + ", shipment type: " + shipmentType + ", and country of origin: " + countryOfOrigin);
        }

        return filteredPosts;
    }


}
