package com.example.bayan;

import com.example.bayan.Api.ApiException;
import com.example.bayan.DTO.IN.Post.AddPostDTO;
import com.example.bayan.DTO.OUT.Post.PostDTO;
import com.example.bayan.Model.*;
import com.example.bayan.Repostiry.*;
import com.example.bayan.Service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BorderRepository borderRepository;

    @Mock
    private AuthRepository authRepository;

    @Mock
    private OfferRepository offerRepository;

    private MyUser myUser;
    private Customer customer;
    private Border border;
    private Post post;
    private AddPostDTO addPostDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        myUser = new MyUser();
        myUser.setId(1);

        customer = new Customer();
        customer.setId(1);
        customer.setUser(myUser);

        border = new Border();
        border.setId(1);
        border.setName("Border A");

        post = new Post();
        post.setId(1);
        post.setTitle("Sample Post");
        post.setCategory("Tech");
        post.setShipmentType("Export");
        post.setCountryOfOrigin("USA");
        post.setCustomer(customer);
        post.setBorder(border);

        addPostDTO = new AddPostDTO();
        addPostDTO.setTitle("New Post");
        addPostDTO.setCategory("Tech");
        addPostDTO.setShipmentType("Export");
        addPostDTO.setCountryOfOrigin("USA");
        addPostDTO.setBorderName("Border A");
    }

    @Test
    void shouldAddPost() {
        when(authRepository.findMyUserById(1)).thenReturn(myUser);
        when(customerRepository.findCustomerByUser(myUser)).thenReturn(customer);
        when(borderRepository.findBorderByName("Border A")).thenReturn(border);
        when(postRepository.findByCustomerAndBorderAndTitleAndShipmentTypeAndCountryOfOriginAndWeight(
                any(), any(), any(), any(), any(), any())).thenReturn(new ArrayList<>());

        postService.addPost(1, addPostDTO);

        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundInAddPost() {
        when(authRepository.findMyUserById(1)).thenReturn(null);

        ApiException exception = assertThrows(ApiException.class, () -> postService.addPost(1, addPostDTO));
        assertThat(exception.getMessage()).isEqualTo("User not found");
    }

    @Test
    void shouldReturnMyPosts() {
        List<Post> posts = new ArrayList<>();
        posts.add(post);

        when(authRepository.findMyUserById(1)).thenReturn(myUser);
        when(customerRepository.findCustomerByUser(myUser)).thenReturn(customer);
        when(postRepository.findAllByCustomerId(1)).thenReturn(posts);

        List<PostDTO> result = postService.myPost(1);

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Sample Post");
    }

    @Test
    void shouldReturnGeneralPostStatisticsForCustomer() {
        List<Post> posts = new ArrayList<>();
        Post post1 = new Post();
        post1.setStatus("Pending");

        Post post2 = new Post();
        post2.setStatus("Accepted");

        posts.add(post1);
        posts.add(post2);

        when(customerRepository.findCustomerById(1)).thenReturn(customer);
        when(postRepository.findAllByCustomer_Id(1)).thenReturn(posts);

        Map<String, Object> stats = postService.getGeneralPostStatisticsForCustomer(1);

        assertThat(stats.get("Total Posts")).isEqualTo(2);
        assertThat(stats.get("Pending Posts")).isEqualTo(1);
        assertThat(stats.get("Accepted Posts")).isEqualTo(1);
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFoundInGetStatistics() {
        when(customerRepository.findCustomerById(1)).thenReturn(null);

        ApiException exception = assertThrows(ApiException.class, () -> postService.getGeneralPostStatisticsForCustomer(1));
        assertThat(exception.getMessage()).isEqualTo("Customer with ID 1 not found.");
    }
}
