package com.example.bayan;

import com.example.bayan.Model.Border;
import com.example.bayan.Model.Customer;
import com.example.bayan.Model.Post;
import com.example.bayan.Repostiry.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    private Post post1;
    private Post post2;
    private Customer customer;
    private Border border;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1);
        customer.setCompanyName("Test Customer");

        border = new Border();
        border.setId(1);
        border.setName("Test Border");

        post1 = new Post();
        post1.setId(1);
        post1.setTitle("Electronics");
        post1.setCategory("Tech");
        post1.setShipmentType("Export");
        post1.setCountryOfOrigin("USA");
        post1.setCustomer(customer);
        post1.setBorder(border);

        post2 = new Post();
        post2.setId(2);
        post2.setTitle("Furniture");
        post2.setCategory("Home");
        post2.setShipmentType("Import");
        post2.setCountryOfOrigin("Canada");
        post2.setCustomer(customer);
        post2.setBorder(border);

        postRepository.save(post1);
        postRepository.save(post2);
    }

    @Test
    void shouldFindAllByCustomerId() {
        List<Post> posts = postRepository.findAllByCustomerId(1);

        assertThat(posts).isNotEmpty();
        assertThat(posts.size()).isEqualTo(2);
        assertThat(posts).contains(post1, post2);
    }

    @Test
    void shouldFindPostById() {
        Post post = postRepository.findPostById(1);

        assertThat(post).isNotNull();
        assertThat(post.getTitle()).isEqualTo("Electronics");
    }

    @Test
    void shouldFindPostsByStatus() {
        post1.setStatus("Pending");
        post2.setStatus("Pending");

        List<Post> posts = postRepository.findPostsByStatus("Pending");

        assertThat(posts).isNotEmpty();
        assertThat(posts.size()).isEqualTo(2);
        assertThat(posts).contains(post1, post2);
    }

    @Test
    void shouldFindPostByCategory() {
        List<Post> posts = postRepository.findPostByCategory("Tech");

        assertThat(posts).isNotEmpty();
        assertThat(posts.size()).isEqualTo(1);
        assertThat(posts.get(0).getTitle()).isEqualTo("Electronics");
    }

    @Test
    void shouldFindPostByCountryOfOrigin() {
        List<Post> posts = postRepository.findPostByCountryOfOrigin("Canada");

        assertThat(posts).isNotEmpty();
        assertThat(posts.size()).isEqualTo(1);
        assertThat(posts.get(0).getTitle()).isEqualTo("Furniture");
    }
}
