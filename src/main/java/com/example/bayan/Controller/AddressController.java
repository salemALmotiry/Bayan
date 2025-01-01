package com.example.bayan.Controller;

import com.example.bayan.Api.ApiResponse;
import com.example.bayan.DTO.IN.CustomsBrokerDTO;
import com.example.bayan.Model.Address;
import com.example.bayan.Service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bayan/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    // add address
    @PostMapping("/add-address/{customer_id}")
    public ResponseEntity<?> addAddress(@PathVariable Integer customer_id, @RequestBody @Valid Address address) {
        addressService.addDeliveryAddress(customer_id, address);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Address added Successfully"));
    }

    // get myAddresses
    @GetMapping("/my-addresses/{customer_id}")
    public  ResponseEntity<?> myAddress(@PathVariable Integer customer_id){
      return ResponseEntity.status(200).body(addressService.myAddresses(customer_id));
    }

    // update
    @PutMapping("/update-address/{customer_id}/address-id/{address_id}")
    public ResponseEntity<?> updateAddress(@PathVariable Integer customer_id,@PathVariable Integer address_id,@RequestBody @Valid Address address){
       addressService.updateAddress(address_id,customer_id,address);
       return ResponseEntity.status(200).body(new ApiResponse("Address updated successfully"));
    }

    // delete
    @DeleteMapping("/delete-address/{customer_id}/address-id/{address_id}")
    public ResponseEntity<?> deleteAddress(@PathVariable Integer customer_id,@PathVariable Integer address_id){
        addressService.deleteAddress(customer_id,address_id);
        return ResponseEntity.status(200).body(new ApiResponse("Address deleted Successfully"));
    }

}
