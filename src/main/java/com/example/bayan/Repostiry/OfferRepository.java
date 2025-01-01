package com.example.bayan.Repostiry;

import com.example.bayan.Model.CustomsBroker;
import com.example.bayan.Model.Offer;
import com.example.bayan.Model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Integer> {

    Offer findOfferById(Integer id);


    List<Offer>getAllOfferByPost(Post post);
}
