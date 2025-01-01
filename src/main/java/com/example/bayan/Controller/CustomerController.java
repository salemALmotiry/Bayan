package com.example.bayan.Controller;

import com.example.bayan.Api.ApiResponse;
import com.example.bayan.DTO.IN.CustomerDTO;
import com.example.bayan.DTO.IN.CustomsBrokerDTO;
import com.example.bayan.Service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bayan/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid CustomerDTO customerDTO) {
        customerService.registerCustomer(customerDTO);
        return ResponseEntity.ok().body(new ApiResponse("Customer registered Successfully"));
    }

    // get .. myAccount
    @GetMapping("/myAccount/{customer_Id}")
    public ResponseEntity<?> myProfile(@PathVariable Integer customer_Id){
        return ResponseEntity.status(200).body(customerService.getMyAccount(customer_Id));
    }


    // update
    @PutMapping("/updateMyAccount/{customerID}")
    public ResponseEntity<?> updateMyAccount(@PathVariable Integer customerID, @RequestBody @Valid CustomerDTO customerDTO){
      customerService.updateCustomerAccount(customerID, customerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Account updated Successfully"));
    }








}
