package com.example.bayan.DTO.OUT;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferDTO {


    private BigDecimal price;

    private Boolean deliveryIncluded;


    private String offerStatus;

    private String productCategory;

    private String borderName;

}
