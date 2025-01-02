package com.example.bayan.Repostiry;

import com.example.bayan.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    Review findReviewById(Integer id);
}
