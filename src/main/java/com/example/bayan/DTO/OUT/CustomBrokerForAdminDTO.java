package com.example.bayan.DTO.OUT;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomBrokerForAdminDTO {

    private String fullName;

    private String licenseNumber;

    private String commercialLicense;

    private String licenseType;

    private Boolean isActive;

    private List<String> borders;
}
