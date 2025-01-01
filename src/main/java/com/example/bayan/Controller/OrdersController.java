package com.example.bayan.Controller;


import com.example.bayan.Service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bayan/offer")
@RequiredArgsConstructor

public class OrdersController {

    private final OrdersService ordersService;

    @PutMapping("/cancel-order/{orderId}/{userId}")
    public ResponseEntity<?> cancelOrder(
            @PathVariable Integer orderId,
            @PathVariable Integer userId) {
        ordersService.cancelOrder(orderId, userId);
        return ResponseEntity.status(200).body("Order canceled successfully");

    }


}
