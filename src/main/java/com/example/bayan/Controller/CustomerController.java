package com.example.bayan.Controller;

import com.example.bayan.Api.ApiResponse;
import com.example.bayan.DTO.IN.CbmDTO;
import com.example.bayan.DTO.IN.CustomerDTO;
import com.example.bayan.DTO.IN.CustomsBrokerDTO;
import com.example.bayan.DTO.IN.UpdateCustomerDTO;
import com.example.bayan.DTO.OUT.CbmResponseDTO;
import com.example.bayan.Model.Notification;
import com.example.bayan.Repostiry.NotificationRepository;
import com.example.bayan.Service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bayan/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final NotificationRepository notificationRepository;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid CustomerDTO customerDTO) {
        customerService.registerCustomer(customerDTO);
        return ResponseEntity.ok().body(new ApiResponse("Customer registered Successfully"));
    }

    // get .. myAccount profile
    @GetMapping("/myAccount/{customer_Id}")
    public ResponseEntity<?> myProfile(@PathVariable Integer customer_Id){
        return ResponseEntity.status(200).body(customerService.getMyAccount(customer_Id));
    }


    // update
    @PutMapping("/updateMyAccount/{customerID}")
    public ResponseEntity<?> updateMyAccount(@PathVariable Integer customerID, @RequestBody @Valid UpdateCustomerDTO customerDTO){
      customerService.updateCustomerAccount(customerID, customerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Account updated Successfully"));
    }



    @PostMapping("/calculate-cbm")
    public ResponseEntity calculateCbm(@RequestBody @Valid CbmDTO cbmDTO) {

        CbmResponseDTO response = customerService.calculateCbm(cbmDTO);

        return ResponseEntity.status(200).body(response);
    }



    // Get all notifications for a customer
    @GetMapping("/getAllMyNotifications/{customerId}")
    public ResponseEntity<?> getAllNotifications(@PathVariable Integer customerId) {
        List<Notification> notifications = customerService.getAllMyNotification(customerId);
        return ResponseEntity.status(200).body(notifications);
    }

    // Mark a notification as read
    @PutMapping("/ReadMyNotifications/{customerId}/{notificationId}/mark-read")
    public ResponseEntity<?> markNotificationAsRead(@PathVariable Integer customerId, @PathVariable Integer notificationId) {
        customerService.markNotification(notificationId, customerId);
        return ResponseEntity.status(200).body(new ApiResponse("Notification marked as read successfully"));
    }







}
