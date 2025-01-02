package com.example.bayan.Service;


import com.example.bayan.Api.ApiException;
import com.example.bayan.Model.CustomsBroker;
import com.example.bayan.Model.Delivery;
import com.example.bayan.Model.MyUser;
import com.example.bayan.Model.Notification;
import com.example.bayan.Repostiry.AuthRepository;
import com.example.bayan.Repostiry.CustomBrokerRepository;
import com.example.bayan.Repostiry.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final CustomBrokerRepository customBrokerRepository;
    private final DeliveryRepository deliveryRepository;
    private final AuthRepository authRepository;


    public void addCarrier(Integer brokerID, Delivery delivery){
        MyUser broker=authRepository.findMyUserById(brokerID);

        if(broker == null){
            throw new ApiException("Broker with ID " + brokerID + " not found.");
        }
        deliveryRepository.save(delivery);

        Notification notification = new Notification();
        notification.setMassage("تم إنشاء طلب توصيل بنجاح");
        notification.setCreateAt(LocalDateTime.now());
        notification.setMyUser(broker);
    }


    public void updateStatus(Integer deliveryID,Integer brokerID, Delivery delivery){
       MyUser broker=authRepository.findMyUserById(brokerID);

        if(broker == null){
            throw new ApiException("Broker with ID " + brokerID + " not found.");
        }

        Delivery oldDelivery=deliveryRepository.findDeliveryById(deliveryID);
        if (oldDelivery == null){
            throw new ApiException("Delivery with ID " + deliveryID + " not found.");
        }

        oldDelivery.setStatus(delivery.getStatus());
        deliveryRepository.save(oldDelivery);

        Notification notification = new Notification();
        notification.setMassage("لقد تم تحديث الشحنة بنجاح");
        notification.setCreateAt(LocalDateTime.now());
         notification.setMyUser(broker);

    }



}
