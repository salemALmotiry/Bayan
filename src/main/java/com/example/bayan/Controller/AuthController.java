package com.example.bayan.Controller;

import com.example.bayan.Api.ApiResponse;
import com.example.bayan.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bayan/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PutMapping("/accept-custom-broker/admin/{adminId}/custom-broker/{customerId}")
    public ResponseEntity<?> acceptCustomBroker(@PathVariable Integer adminId, @PathVariable Integer customerId) {
        authService.acceptCustomBroker(adminId, customerId);
        return ResponseEntity.status(200).body(new ApiResponse("Customs Broker has been activated successfully."));
    }

    @PutMapping("/reject-custom-broker/admin/{adminId}/custom-broker/{customerId}")
    public ResponseEntity<?> rejectCustomBroker(
            @PathVariable Integer adminId,
            @PathVariable Integer customerId) {
        authService.rejectCustomBroker(adminId, customerId);
        return ResponseEntity.status(200).body(new ApiResponse("Customs Broker has been activated successfully."));
    }
}
