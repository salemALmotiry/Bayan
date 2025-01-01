package com.example.bayan.Service;

import com.example.bayan.Api.ApiException;
import com.example.bayan.Model.MyUser;
import com.example.bayan.Model.Offer;
import com.example.bayan.Model.Orders;
import com.example.bayan.Repostiry.AuthRepository;
import com.example.bayan.Repostiry.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        for (Offer offer : order.getOffers()) {
            if (offer.getPost().getCustomer().getUser().getId().equals(userId)) {
                belongsToCustomer = true;
                break;
            }
        }

        if (!belongsToCustomer) {
            throw new ApiException("Order does not belong to the customer with ID " + userId);
        }

        // Check if the order status is "PLACED"
        if (!"PLACED".equals(order.getStatus())) {
            throw new ApiException("Order with ID " + orderId + " cannot be canceled as it is not in 'PLACED' status.");
        }

        // Cancel the order
        order.setStatus("CANCELED");
        ordersRepository.save(order);
    }


}
