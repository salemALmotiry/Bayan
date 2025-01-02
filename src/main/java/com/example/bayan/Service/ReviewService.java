package com.example.bayan.Service;

import com.example.bayan.Api.ApiException;
import com.example.bayan.DTO.IN.Post.AddPostDTO;
import com.example.bayan.DTO.IN.ReviewBrokerDTO;
import com.example.bayan.DTO.IN.ReviewCustomerDTO;
import com.example.bayan.DTO.OUT.Post.PostDTO;
import com.example.bayan.Model.*;
import com.example.bayan.Repostiry.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final OrdersRepository ordersRepository;
    private final ReviewRepository reviewRepository;
    private final CustomerRepository customerRepository;
    private final CustomBrokerRepository brokerRepository;

    private final List<String> statusSequence = List.of(
            "PLACED",
            "UNDER_REVIEW",
            "AWAITING_CLEARANCE",
            "CLEARING_IN_PROGRESS",
            "COMPLETED"
    );

    // Broker rates customer
    public void brokerRateCustomer(Integer orderId, ReviewCustomerDTO reviewDTO) {
        Orders order = ordersRepository.findOrdersById(orderId);
        if (order == null) {
            throw new ApiException("Order not found");
        }

        CustomsBroker broker = order.getOffer().getBroker();
        if (!broker.getId().equals(reviewDTO.getReviewerBrokerId())) {
            throw new ApiException("Unauthorized broker for this order.");
        }

        Review review = new Review();
        review.setOrder(order);
        review.setRating(reviewDTO.getRating());
        review.setReviewerBroker(broker);
        review.setReviewedCustomer(order.getOffer().getPost().getCustomer());

        reviewRepository.save(review);
    }

    // Customer rates broker
    public void customerRateBroker(Integer orderId, ReviewBrokerDTO reviewDTO) {
        Orders order = ordersRepository.findOrdersById(orderId);
        if (order == null) {
            throw new ApiException("Order not found");
        }

        Customer customer = order.getOffer().getPost().getCustomer();
        if (!customer.getId().equals(reviewDTO.getReviewerCustomerId())) {
            throw new ApiException("Unauthorized customer for this order.");
        }

        Review review = new Review();
        review.setOrder(order);
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review.setReviewerCustomer(customer);
        review.setReviewedBroker(order.getOffer().getBroker());

        reviewRepository.save(review);
    }

    // Update broker review for customer
    public void updateBrokerReview(Integer reviewId, ReviewCustomerDTO reviewDTO) {
        Review review = reviewRepository.findReviewById(reviewId);
        if (review == null) {
            throw new ApiException("Review not found");
        }

        if (review.getReviewerBroker() == null) {
            throw new ApiException("This review is not created by a broker.");
        }

        review.setRating(reviewDTO.getRating());

        reviewRepository.save(review);
    }

    // Update customer review for broker
    public void updateCustomerReview(Integer reviewId, ReviewBrokerDTO reviewDTO) {
        Review review = reviewRepository.findReviewById(reviewId);
        if (review == null) {
            throw new ApiException("Review not found");
        }

        if (review.getReviewerCustomer() == null) {
            throw new ApiException("This review is not created by a customer.");
        }

        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());

        reviewRepository.save(review);
    }

    // Update order status
    public void updateOrderStatus(Integer orderId, Integer userId) {
        Orders order = ordersRepository.findOrdersById(orderId);
        if (order == null) {
            throw new ApiException("Order not found");
        }

        String currentStatus = order.getStatus();
        int currentIndex = statusSequence.indexOf(currentStatus);

        if (currentIndex == -1) {
            throw new ApiException("Invalid order status: " + currentStatus);
        }
        if (currentIndex == statusSequence.size() - 1) {
            throw new ApiException("Order is already in the final status: " + currentStatus);
        }

        order.setStatus(statusSequence.get(currentIndex + 1));
        ordersRepository.save(order);
    }
}

