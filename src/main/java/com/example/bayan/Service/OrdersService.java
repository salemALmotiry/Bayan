package com.example.bayan.Service;

import com.example.bayan.Api.ApiException;
import com.example.bayan.DTO.OUT.OrderDTO;
import com.example.bayan.DTO.OUT.Post.PostDTO;
import com.example.bayan.Model.*;
import com.example.bayan.Repostiry.*;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class OrdersService {

    private final AuthRepository authRepository;
    private final CustomerRepository customerRepository;
    private final OrdersRepository ordersRepository;
    private final OfferRepository offerRepository;
    private final CustomBrokerRepository customBrokerRepository;

    public void cancelOrder(Integer orderId, Integer userId) {
        // Validate the user
        MyUser customer = authRepository.findMyUserById(userId);
        if (customer == null) {
            throw new ApiException("Customer with ID " + userId + " not found.");
        }

        // Validate the order
        Orders order = ordersRepository.findOrdersByIdAndOffer_Post_CustomerId(orderId,customer.getId());

        if (order == null) {
           throw new ApiException("Order with ID " + orderId + " not found.");
        }


        boolean belongsToCustomer = false;

        // Fetch the single offer associated with the order
        Offer offer = order.getOffer(); // Assuming `getOffer()` retrieves the single offer in the One-to-One relationship

        if (offer != null && offer.getPost().getCustomer().getUser().getId().equals(userId)) {
            belongsToCustomer = true;
            offer.setOfferStatus("Rejected");
        }


        if (!belongsToCustomer) {
            throw new ApiException("Order does not belong to the customer with ID " + userId);
        }

        // Check if the order status is "PLACED"
        if (!"Placed".equals(order.getStatus())) {
            throw new ApiException("Order with ID " + orderId + " cannot be canceled as it is not in 'PLACED' status.");
        }

        order.setStatus("Canceled");
        ordersRepository.save(order);
    }


    public void cancelOrderBroker(Integer orderId, Integer userId) {
        // Validate the user
        MyUser broker = authRepository.findMyUserById(userId);
        if (broker == null) {
            throw new ApiException("Customer with ID " + userId + " not found.");
        }

        // Validate the order
        Orders order = ordersRepository.findOrdersByIdAndOffer_BrokerId(orderId,broker.getId());

        if (order == null) {
            throw new ApiException("Order with ID " + orderId + " not found.");
        }



        boolean belongsToCustomer = false;

        // Fetch the single offer associated with the order
        Offer offer = order.getOffer(); // Assuming `getOffer()` retrieves the single offer in the One-to-One relationship

        if (offer != null && offer.getBroker().getUser().getId().equals(userId)) {
            belongsToCustomer = true;
            offer.setOfferStatus("Rejected");
        }


        if (!belongsToCustomer) {
            throw new ApiException("Order does not belong to the customer with ID " + userId);
        }

        // Check if the order status is "PLACED"
        if (!"Placed".equals(order.getStatus())) {
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

        Orders order = ordersRepository.findOrdersByIdAndOffer_BrokerId(orderId,broker.getId());

        if (order == null) {
            throw new ApiException("Order with ID " + orderId + " not found.");
        }

        // Define the customs clearance status progression
        List<String> statusSequence = List.of(
                "Placed",
                "Under_review",
                "Awaiting_clearance",
                "Clearing_in_progress",
                "Completed"
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
            Offer offer =  order.getOffer();
            offer.setOfferStatus("Completed");
            offerRepository.save(offer);
            throw new ApiException("Order is already in the final status: " + currentStatus);
        }

        // Update to the next status
        String nextStatus = statusSequence.get(currentIndex + 1);
        order.setStatus(nextStatus);

        // Save the updated order

        ordersRepository.save(order);
    }


    public void setPaymentCompleted(Integer orderId, Integer brokerId){
        MyUser broker = authRepository.findMyUserById(brokerId);

        if (broker == null) {
            throw new ApiException("Broker with ID " + brokerId + " not found.");
        }

        Orders order = ordersRepository.findOrdersByIdAndOffer_BrokerId(orderId,broker.getId());

        if (order == null) {
            throw new ApiException("Order with ID " + orderId + " not found.");
        }

        if(!order.getPaymentStatus().equals("Waiting for approve")){
            throw new ApiException("Payment is not waiting for approve");
        }
        order.setPaymentStatus("Completed");

        ordersRepository.save(order);

    }



    public void setPaymentWaitingForApprove(Integer orderId, Integer customerId){
        MyUser customer = authRepository.findMyUserById(customerId);

        if (customer == null) {
            throw new ApiException("Broker with ID " + customerId + " not found.");
        }
        Orders order = ordersRepository.findOrdersByIdAndOffer_Post_CustomerId(orderId,customer.getId());

        if (order == null) {
            throw new ApiException("Order with ID " + orderId + " not found.");
        }

        if(!order.getPaymentStatus().equals("Pending")){
            throw new ApiException("Payment is not Pending");
        }
        order.setPaymentStatus("Waiting for approve");
        ordersRepository.save(order);


    }

    public List<OrderDTO> myOrders(Integer customerId){
        MyUser user = authRepository.findMyUserById(customerId);
        if (user == null) {
            throw new ApiException("Wrong customer Id");
        }
        Customer customer = customerRepository.findCustomerByUser(user);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }
        List<Orders> orders = ordersRepository.findAllByOffer_Post_CustomerId(customer.getId());

        List<OrderDTO> orderDTOS = new ArrayList<>();

        for (Orders order : orders) {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setStatus(order.getStatus());
            orderDTO.setBrokerName(order.getOffer().getBroker().getUser().getFullName());
            orderDTO.setBorderName(order.getOffer().getPost().getBorder().getName());
            orderDTO.setShipmentPrice(order.getOffer().getPrice());
            orderDTO.setHasDelivery(order.getOffer().getDeliveryIncluded());
            orderDTO.setPaymentStatus(order.getPaymentStatus());
            orderDTOS.add(orderDTO);
        }
        return orderDTOS;
    }

    public OrderDTO orderDetailsForCustomer(Integer customerId ,Integer orderId){
        MyUser user = authRepository.findMyUserById(customerId);
        if (user == null) {
            throw new ApiException("Wrong customer Id");
        }
        Customer customer = customerRepository.findCustomerByUser(user);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }
        Orders order = ordersRepository.findOrdersByIdAndCustomerId(orderId,customerId);
        if (order == null) {
            throw new ApiException("Order not found for the given customer");
        }
        OrderDTO orderDTO = new OrderDTO(order.getStatus(),order.getOffer().getDeliveryIncluded(),order.getOffer().getBroker().getUser().getFullName(),order.getOffer().getPost().getBorder().getName(),order.getOffer().getPrice(),order.getPaymentStatus());
        return orderDTO;
    }

    public OrderDTO orderDetailsForBroker(Integer brokerId ,Integer orderId){
        MyUser user = authRepository.findMyUserById(brokerId);
        if (user == null) {
            throw new ApiException("Wrong customer Id");
        }
        CustomsBroker broker = customBrokerRepository.findCustomsBrokerByUser(user);
        if (broker == null) {
            throw new ApiException("Broker not found");
        }
        Orders order = ordersRepository.findOrdersByIdAndBrokerId(orderId,brokerId);
        if (order == null) {
            throw new ApiException("Order not found for the given broker");
        }
        OrderDTO orderDTO = new OrderDTO(order.getStatus(),order.getOffer().getDeliveryIncluded(),order.getOffer().getBroker().getUser().getFullName(),order.getOffer().getPost().getBorder().getName(),order.getOffer().getPrice(),order.getPaymentStatus());
        return orderDTO;
    }

    public List<OrderDTO> myOrdersAsBroker(Integer brokerId){
        MyUser user = authRepository.findMyUserById(brokerId);
        if (user == null) {
            throw new ApiException("Wrong customer Id");
        }
        CustomsBroker broker = customBrokerRepository.findCustomsBrokerByUser(user);
        if (broker == null) {
            throw new ApiException("Broker not found");
        }
        List<Orders> orders = ordersRepository.findAllByOffer_Post_CustomerId(broker.getId());

        List<OrderDTO> orderDTOS = new ArrayList<>();

        for (Orders order : orders) {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setStatus(order.getStatus());
            orderDTO.setBrokerName(order.getOffer().getBroker().getUser().getFullName());
            orderDTO.setBorderName(order.getOffer().getPost().getBorder().getName());
            orderDTO.setShipmentPrice(order.getOffer().getPrice());
            orderDTO.setHasDelivery(order.getOffer().getDeliveryIncluded());
            orderDTO.setPaymentStatus(order.getPaymentStatus());
            orderDTOS.add(orderDTO);
        }
        return orderDTOS;
    }

}
