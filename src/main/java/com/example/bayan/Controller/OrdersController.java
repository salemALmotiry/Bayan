package com.example.bayan.Controller;


import com.example.bayan.Api.ApiResponse;
import com.example.bayan.Service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bayan/order")
@RequiredArgsConstructor

public class OrdersController {

    private final OrdersService ordersService;

    @PutMapping("/cancel-order/{orderId}/{userId}")
    public ResponseEntity<?> cancelOrder(@PathVariable Integer orderId, @PathVariable Integer userId) {
        ordersService.cancelOrder(orderId, userId);
        return ResponseEntity.status(200).body(new ApiResponse("Order canceled successfully"));

    }

    @PutMapping("/update-order-status/{orderId}/{userId}")
    public ResponseEntity updateOrderStatus(@PathVariable Integer orderId, @PathVariable Integer userId) {

        ordersService.updateOrderStatus(orderId, userId);

        return ResponseEntity.status(200).body(new ApiResponse("Order status updated successfully"));
    }



}
