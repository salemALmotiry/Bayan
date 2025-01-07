package com.example.bayan.Controller;


import com.example.bayan.Api.ApiResponse;
import com.example.bayan.Model.MyUser;
import com.example.bayan.Service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bayan/order")
@RequiredArgsConstructor

public class OrdersController {

    private final OrdersService ordersService;

    @PutMapping("/cancel-order/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable Integer orderId, @AuthenticationPrincipal MyUser user) {
        ordersService.cancelOrder(orderId, user.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Order canceled successfully"));
    }


    @PutMapping("/update-order-status/{orderId}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Integer orderId, @AuthenticationPrincipal MyUser user) {
        ordersService.updateOrderStatus(orderId, user.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Order status updated successfully"));
    }

    @PutMapping("/cancel-order-broker/{orderId}")
    public ResponseEntity<?> cancelOrderBroker(@PathVariable Integer orderId, @AuthenticationPrincipal MyUser broker) {
        ordersService.cancelOrderBroker(orderId, broker.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Order canceled successfully by broker"));
    }

    @PutMapping("/set-payment-completed/{orderId}")
    public ResponseEntity<?> setPaymentCompleted(@AuthenticationPrincipal MyUser broker,
                                                 @PathVariable Integer orderId) {
        ordersService.setPaymentCompleted(orderId, broker.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Payment status set to Completed successfully."));
    }

    @PutMapping("/set-payment-waiting-for-approve/{orderId}")
    public ResponseEntity<?> setPaymentWaitingForApprove(@AuthenticationPrincipal MyUser customer,
                                                         @PathVariable Integer orderId) {
        ordersService.setPaymentWaitingForApprove(orderId, customer.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Payment status set to Waiting for approve successfully."));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<?> myOrders(@AuthenticationPrincipal MyUser customer){
        return ResponseEntity.status(200).body(ordersService.myOrders(customer.getId()));
    }

    @GetMapping("/order-details/customer/{orderId}")
    public ResponseEntity<?> orderDetailsForCustomer(@AuthenticationPrincipal MyUser customer, @PathVariable Integer orderId){
        return ResponseEntity.status(200).body(ordersService.orderDetailsForCustomer(customer.getId(),orderId));
    }

    @GetMapping("/order-details/broker/{orderId}")
    public ResponseEntity<?> orderDetailsForBroker(@AuthenticationPrincipal MyUser broker, @PathVariable Integer orderId){
        return ResponseEntity.status(200).body(ordersService.orderDetailsForBroker(broker.getId(),orderId));
    }

    @GetMapping("/my-orders-as-broker")
    public ResponseEntity<?> myOrdersAsBroker(@AuthenticationPrincipal MyUser broker){
        return ResponseEntity.status(200).body(ordersService.myOrdersAsBroker(broker.getId()));
    }

}
