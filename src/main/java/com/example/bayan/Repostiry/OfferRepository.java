package com.example.bayan.Repostiry;

import com.example.bayan.Model.CustomsBroker;
import com.example.bayan.Model.Offer;
import com.example.bayan.Model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Integer> {

    Offer findOfferById(Integer id);


    Offer findOfferByIdAndPost_CustomerId(Integer id, Integer customerId);
    List<Offer>getAllOfferByPost(Post post);

    Offer findOfferByIdAndBrokerId(Integer id, Integer broker_id);

    Boolean existsByPostAndBrokerAndOfferStatus(Post post, CustomsBroker broker, String offerStatus);
}
