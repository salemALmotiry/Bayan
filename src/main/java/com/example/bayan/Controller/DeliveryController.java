package com.example.bayan.Controller;

import com.example.bayan.Api.ApiResponse;
import com.example.bayan.DTO.IN.DeliveryDTO;
import com.example.bayan.Model.Delivery;
import com.example.bayan.Service.DeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bayan/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    // Add a new carrier for a delivery
    @PostMapping("/add-carrier/{brokerId}/{orderId}")
    public ResponseEntity<?> addCarrier(@PathVariable Integer brokerId,@PathVariable Integer orderId ,@RequestBody @Valid DeliveryDTO delivery) {
        deliveryService.addCarrier(brokerId,orderId, delivery);
        return ResponseEntity.status(200).body(new ApiResponse("Carrier added successfully"));
    }
//
    // Update delivery status
    @PutMapping("/update-status/{deliveryId}/{brokerId}")
    public ResponseEntity<?> updateDeliveryStatus(@PathVariable Integer deliveryId, @PathVariable Integer brokerId) {
        deliveryService.updateStatus(deliveryId, brokerId);
        return ResponseEntity.status(200).body(new ApiResponse("Delivery status updated successfully"));
    }
}
