package com.example.bayan.Service;


import com.example.bayan.Api.ApiException;
import com.example.bayan.DTO.IN.DeliveryDTO;
import com.example.bayan.Model.*;
import com.example.bayan.Repostiry.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final CustomBrokerRepository customBrokerRepository;
    private final DeliveryRepository deliveryRepository;
    private final AuthRepository authRepository;
    private final OrdersRepository ordersRepository;
    private final OfferRepository offerRepository;
    private final NotificationRepository notificationRepository;


    public void addCarrier(Integer brokerID, Integer orderId,DeliveryDTO deliveryDTO){
        MyUser broker=authRepository.findMyUserById(brokerID);

        if(broker == null){
            throw new ApiException("Broker with ID " + brokerID + " not found.");
        }
        Offer offer = offerRepository.findOfferByIdAndBrokerId(orderId,brokerID);

        if (offer == null) {
            throw new ApiException("Order with ID " + orderId + " not found.");
        }
        Orders order = ordersRepository.findOrdersById(orderId);

        if (!order.getStatus().equals("COMPLETED")) {
            throw new ApiException("Order with ID " + orderId + " is not completed.");
        }

        Delivery delivery = new Delivery();

        delivery.setCarrier(deliveryDTO.getCarrier());
        delivery.setTrackingNumber(deliveryDTO.getTrackingNumber());

        delivery.setOrder(order);
        deliveryRepository.save(delivery);

        Notification notification = new Notification();
        notification.setMassage("تم إنشاء طلب توصيل بنجاح");
        notification.setCreateAt(LocalDateTime.now());
        notification.setMyUser(broker);
    }


    public void updateStatus(Integer deliveryID, Integer brokerID) {
        // Fetch the broker
        MyUser broker = authRepository.findMyUserById(brokerID);
        if (broker == null)
            throw new ApiException("Broker with ID " + brokerID + " not found.");


        // Fetch the delivery
        Delivery oldDelivery = deliveryRepository.findDeliveryById(deliveryID);

        if (oldDelivery == null)
            throw new ApiException("Delivery with ID " + deliveryID + " not found.");

        Orders order = oldDelivery.getOrder();
        
        if (!order.getStatus().equals("COMPLETED"))
            throw new ApiException("order with ID " + order.getId() + " not COMPLETED.");
            

        
        if (!Objects.equals(order.getOffer().getBroker().getId(), broker.getId()))
            throw new ApiException("Order with ID " + order.getId() + " is not .");

        
        // Determine the next status
        String currentStatus = oldDelivery.getStatus();
        String nextStatus = getNextStatus(currentStatus);

        if (nextStatus == null) {
            throw new ApiException("Delivery is already in the final status and cannot be updated further.");
        }

        // Update the delivery status
        oldDelivery.setStatus(nextStatus);
        deliveryRepository.save(oldDelivery);

        // Create a notification
        Notification notification = new Notification();
        notification.setMassage("تم تحديث حالة الشحنة إلى: " + nextStatus);
        notification.setCreateAt(LocalDateTime.now());
        notification.setMyUser(broker);

        // Save or send the notification (assumes a notification repository or service exists)
        notificationRepository.save(notification);
    }

    // Helper method to determine the next status
    private String getNextStatus(String currentStatus) {
        return switch (currentStatus) {
            case "STARTED" -> "IN_PROGRESS";
            case "IN_PROGRESS" -> "COMPLETED";
            case "COMPLETED" -> null; // Final status, no further transitions
            default -> "STARTED"; // Default to STARTED if status is null or unknown
        };
    }



}
