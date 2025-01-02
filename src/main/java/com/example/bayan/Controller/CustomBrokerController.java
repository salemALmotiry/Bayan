package com.example.bayan.Controller;


import com.example.bayan.Api.ApiResponse;
import com.example.bayan.DTO.IN.CustomsBrokerDTO;
import com.example.bayan.Model.CustomsBroker;
import com.example.bayan.Service.CustomBrokerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bayan/custom-broker")
@RequiredArgsConstructor
public class CustomBrokerController {

    private final CustomBrokerService brokerService;

    // register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid CustomsBrokerDTO customsBrokerDTO){
      brokerService.register(customsBrokerDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Registered Successfully"));
    }

    // get .. myProfile
    @GetMapping("/my-profile/broker-id/{broker_id}")
    public ResponseEntity<?> myProfile(@PathVariable Integer broker_id){
       return ResponseEntity.status(200).body(brokerService.myProfile(broker_id));
    }

    // update
    @PutMapping("/update-my-account/broker-id/{broker_id}")
    public ResponseEntity<?> updateMyAccount(@PathVariable Integer broker_id , @RequestBody @Valid CustomsBrokerDTO customsBrokerDTO){
        brokerService.updateMyAccount(broker_id , customsBrokerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Account updated Successfully"));
    }
    // delete
    @DeleteMapping("/delete-my-account/broker-id/{broker_id}")
    public ResponseEntity<?> deleteMyAccount(@PathVariable Integer broker_id){
        brokerService.deleteMyAccount(broker_id);
        return ResponseEntity.status(200).body(new ApiResponse("Account deleted Successfully"));
    }


    // Get Customs Broker by License Number
    @GetMapping("/license-number/{licenseNumber}")
    public ResponseEntity<?> getByLicenseNumber(@PathVariable String licenseNumber) {
        CustomsBroker customsBroker = brokerService.getByLicenseNumber(licenseNumber);
        return ResponseEntity.status(200).body(customsBroker);
    }

    // Get all Customs Brokers working at a specific border
    @GetMapping("/border/{border}")
    public ResponseEntity<?> getAllCustomsByBorder(@PathVariable String border) {
        List<CustomsBroker> customsBrokers = brokerService.getAllCustomsByBorder(border);
        return ResponseEntity.status(200).body(customsBrokers);
    }

    // Get all Customs Brokers by name
    @GetMapping("/name/{name}")
    public ResponseEntity<?> getAllCustomsByName(@PathVariable String name) {
        List<CustomsBroker> customsBrokers = brokerService.getAllCustomsByName(name);
        return ResponseEntity.status(200).body(customsBrokers);
    }

    // Get all Customs Brokers by License Type
    @GetMapping("/license-type/{type}")
    public ResponseEntity<?> getAllCustomsByLicenseType(@PathVariable String type) {
        List<CustomsBroker> customsBrokers = brokerService.getAllCustomsBYLicenseType(type);
        return ResponseEntity.status(200).body(customsBrokers);
    }
}
