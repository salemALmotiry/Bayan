package com.example.bayan.Service;


import com.example.bayan.Api.ApiException;
import com.example.bayan.DTO.IN.Post.AddPostDTO;
import com.example.bayan.DTO.OUT.Post.PostDTO;
import com.example.bayan.Model.Border;
import com.example.bayan.Model.Customer;
import com.example.bayan.Model.MyUser;
import com.example.bayan.Model.Post;
import com.example.bayan.Repostiry.AuthRepository;
import com.example.bayan.Repostiry.BorderRepository;
import com.example.bayan.Repostiry.CustomerRepository;
import com.example.bayan.Repostiry.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CustomerRepository customerRepository;
    private final BorderRepository borderRepository;
    private final AuthRepository authRepository;


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


}
