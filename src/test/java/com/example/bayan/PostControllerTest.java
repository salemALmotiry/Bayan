package com.example.bayan;

import com.example.bayan.Api.ApiResponse;
import com.example.bayan.Controller.PostController;
import com.example.bayan.DTO.IN.Post.AddPostDTO;
import com.example.bayan.DTO.OUT.Post.PostDTO;
import com.example.bayan.Model.MyUser;
import com.example.bayan.Service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = PostController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class PostControllerTest {

    @MockBean
    PostService postService;

    @Autowired
    MockMvc mockMvc;

    MyUser mockUser;
    AddPostDTO mockAddPostDTO;
    PostDTO postDTO1, postDTO2;
    List<PostDTO> postList;

    @BeforeEach
    void setUp() {
        // إعداد المستخدم الوهمي
        mockUser = new MyUser();
        mockUser.setId(1);
        mockUser.setUsername("testUser");

        // إعداد بيانات الإدخال
        mockAddPostDTO = new AddPostDTO();
        mockAddPostDTO.setTitle("Electronic Goods21111");
        mockAddPostDTO.setCategory("Electronics");
        mockAddPostDTO.setShipmentType("Air Freight");
        mockAddPostDTO.setBillOfLading("12345618901");
        mockAddPostDTO.setCountryOfOrigin("الصين");
        mockAddPostDTO.setBorderName("ميناء جدة الإسلامي");
        mockAddPostDTO.setWeight(25.5);
        mockAddPostDTO.setHasDocuments(true);
        mockAddPostDTO.setHasDelivery(false);

        // إعداد بيانات المنشورات (PostDTO)
        postDTO1 = new PostDTO();
        postDTO1.setTitle("Electronic Goods21111");
        postDTO1.setCategory("Electronics");
        postDTO1.setWeight(25.5);
        postDTO1.setShipmentType("Air Freight");
        postDTO1.setCountryOfOrigin("الصين");
        postDTO1.setStatus("Pending");
        postDTO1.setHasDelivery(false);
        postDTO1.setHasDocuments(true);
        postDTO1.setBorderName("ميناء جدة الإسلامي");

        postDTO2 = new PostDTO();
        postDTO2.setTitle("Post 2");
        postDTO2.setCategory("Category 2");
        postDTO2.setWeight(10.0);
        postDTO2.setShipmentType("Sea Freight");
        postDTO2.setCountryOfOrigin("USA");
        postDTO2.setStatus("Approved");
        postDTO2.setHasDelivery(true);
        postDTO2.setHasDocuments(false);
        postDTO2.setBorderName("Port of New York");

        postList = Arrays.asList(postDTO1, postDTO2);
    }

    @Test
    public void testGetAllPosts() throws Exception {
        Mockito.when(postService.getAllPosts()).thenReturn(postList);

        mockMvc.perform(get("/api/v1/bayan/post/get-all-posts"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Post 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Post 2"));
    }

    @Test
    public void testAddPost() throws Exception {
        mockMvc.perform(post("/api/v1/bayan/post/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockAddPostDTO)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Post created successfully"));
    }

    @Test
    public void testDeletePost() throws Exception {
        mockMvc.perform(delete("/api/v1/bayan/post/delete/{postId}", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Post deleted successfully"));
    }

    @Test
    public void testUpdatePost() throws Exception {
        mockMvc.perform(put("/api/v1/bayan/post/update/{postId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockAddPostDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Post updated successfully"));
    }

    @Test
    public void testGetMyPosts() throws Exception {
        Mockito.when(postService.myPost(mockUser.getId())).thenReturn(postList);

        mockMvc.perform(get("/api/v1/bayan/post/my-posts"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Post 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Post 2"));
    }
}
