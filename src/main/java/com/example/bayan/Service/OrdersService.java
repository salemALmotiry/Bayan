package com.example.bayan.Service;

import com.example.bayan.Api.ApiException;
import com.example.bayan.Model.MyUser;
import com.example.bayan.Model.Offer;
import com.example.bayan.Model.Orders;
import com.example.bayan.Repostiry.AuthRepository;
import com.example.bayan.Repostiry.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class OrdersService {

    private final AuthRepository authRepository;
    private final OrdersRepository ordersRepository;

    public void cancelOrder(Integer orderId, Integer userId) {
        // Validate the user
        MyUser customer = authRepository.findMyUserById(userId);
        if (customer == null) {
            throw new ApiException("Customer with ID " + userId + " not found.");
        }

        // Validate the order
        Orders order = ordersRepository.findOrdersById(orderId);

        if (order == null) {
           throw new ApiException("Order with ID " + orderId + " not found.");
        }


        boolean belongsToCustomer = false;

        // Fetch the single offer associated with the order
        Offer offer = order.getOffer(); // Assuming `getOffer()` retrieves the single offer in the One-to-One relationship

        if (offer != null && offer.getPost().getCustomer().getUser().getId().equals(userId)) {
            belongsToCustomer = true;
            offer.setOfferStatus("Canceled");
        }


        if (!belongsToCustomer) {
            throw new ApiException("Order does not belong to the customer with ID " + userId);
        }

        // Check if the order status is "PLACED"
        if (!"PLACED".equals(order.getStatus())) {
            throw new ApiException("Order with ID " + orderId + " cannot be canceled as it is not in 'PLACED' status.");
        }

        order.setStatus("Canceled");
        ordersRepository.save(order);
    }




    public void updateOrderStatus(Integer orderId, Integer userId) {
        MyUser broker = authRepository.findMyUserById(userId);

        if (broker == null) {
            throw new ApiException("Broker with ID " + userId + " not found.");
        }

        Orders order = ordersRepository.findOrdersById(orderId);

        if (order == null) {
            throw new ApiException("Order with ID " + orderId + " not found.");
        }

        // Define the customs clearance status progression
        List<String> statusSequence = List.of(
                "PLACED",
                "UNDER_REVIEW",
                "AWAITING_CLEARANCE",
                "CLEARING_IN_PROGRESS",
                "COMPLETED"
        );

        // Get the current status
        String currentStatus = order.getStatus();

        // Find the current index in the sequence
        int currentIndex = statusSequence.indexOf(currentStatus);
        if (currentIndex == -1) {
            throw new ApiException("Invalid order status: " + currentStatus);
        }

        // Check if the order has already reached the final status
        if (currentIndex == statusSequence.size() - 1) {
            throw new ApiException("Order is already in the final status: " + currentStatus);
        }

        // Update to the next status
        String nextStatus = statusSequence.get(currentIndex + 1);
        order.setStatus(nextStatus);

        // Save the updated order
        ordersRepository.save(order);
    }



}
