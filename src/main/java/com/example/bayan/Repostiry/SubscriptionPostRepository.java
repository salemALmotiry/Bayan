package com.example.bayan.Repostiry;

import com.example.bayan.Model.Post;
import com.example.bayan.Model.SubscriptionPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionPostRepository extends JpaRepository<SubscriptionPost,Integer> {

    List<SubscriptionPost> findAllByCustomerId(Integer userId);

    SubscriptionPost findPostById(Integer postId);

}
