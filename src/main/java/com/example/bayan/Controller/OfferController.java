package com.example.bayan.Controller;


import com.example.bayan.Api.ApiResponse;
import com.example.bayan.DTO.IN.CustomerDTO;
import com.example.bayan.DTO.IN.Offer.OfferDTO;
import com.example.bayan.DTO.IN.Offer.OfferWithDeliveryDTO;
import com.example.bayan.DTO.OUT.CustomerOfferDTO;
import com.example.bayan.Service.CustomerService;
import com.example.bayan.Service.OfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bayan/offer")
@RequiredArgsConstructor

public class OfferController {


    private final OfferService offerService;

    // broker offer without delivery
    @PostMapping("/create-offer/{userId}")
    public ResponseEntity createOffer(@PathVariable Integer userId, @RequestBody @Valid OfferDTO offerDTO) {

        offerService.createOffer(userId, offerDTO);

        return ResponseEntity.status(200).body(new ApiResponse("Offer created successfully"));
    }

    @PutMapping("/update-offer/{userId}/{offerId}")
    public ResponseEntity updateOffer(@PathVariable Integer userId, @PathVariable Integer offerId, @RequestBody @Valid OfferDTO offerDTO) {

        offerService.updateOffer(userId, offerId, offerDTO);

        return ResponseEntity.status(200).body(new ApiResponse("Offer updated successfully"));
    }

    @DeleteMapping("/delete-offer/{userId}/{offerId}")
    public ResponseEntity deleteOffer(@PathVariable Integer userId, @PathVariable Integer offerId) {

        offerService.deleteOffer(userId, offerId);

        return ResponseEntity.status(200).body(new ApiResponse("Offer deleted successfully"));
    }

    //  _______________ broker offer with delivery_____________

    @PostMapping("/create-offer-with-delivery/{userId}")
    public ResponseEntity createOfferWithDelivery(@PathVariable Integer userId, @RequestBody @Valid OfferWithDeliveryDTO offerDTO) {

        offerService.createOfferWithDelivery(userId, offerDTO);

        return ResponseEntity.status(200).body(new ApiResponse("Offer with delivery created successfully"));
    }

    @PutMapping("/update-offer-with-delivery/{userId}/{offerId}")
    public ResponseEntity updateOfferWithDelivery(@PathVariable Integer userId, @PathVariable Integer offerId, @RequestBody @Valid OfferWithDeliveryDTO offerDTO) {

        offerService.updateOfferWithDelivery(userId, offerId, offerDTO);

        return ResponseEntity.status(200).body(new ApiResponse("Offer with delivery updated successfully"));
    }

    @DeleteMapping("/delete-offer-with-delivery/{userId}/{offerId}")
    public ResponseEntity removeOffer(@PathVariable Integer userId, @PathVariable Integer offerId) {

        offerService.removeOffer(userId, offerId);

        return ResponseEntity.status(200).body(new ApiResponse("Offer removed successfully"));
    }


    //_________________________________________________

    @PutMapping("/accept-offer/{userId}/{offerId}")
    public ResponseEntity<?> acceptOffer(@PathVariable Integer userId, @PathVariable Integer offerId) {
        offerService.acceptOffer(userId, offerId);
        return ResponseEntity.status(200).body("Offer accepted successfully");
    }


//    @GetMapping("/my-offers/{postId}/{userId}")
//    public ResponseEntity<List<CustomerOfferDTO>> getMyOffers(@PathVariable Integer postId,@PathVariable Integer userId) {
//        List<CustomerOfferDTO> offers = offerService.getAllOffersforOnePost(postId,userId);
//        return ResponseEntity.status(200).body(offers);
//    }



    // Get all offers for a specific post for a customer
    @GetMapping("/AllOffer-post/{post_id}/customer/{customer_id}")
    public ResponseEntity<?> getAllOffersForOnePost(@PathVariable Integer post_id, @PathVariable Integer customer_id) {
        List<CustomerOfferDTO> offers = offerService.getAllOffersforOnePost(post_id, customer_id);
        return ResponseEntity.status(200).body(offers);
    }


}
