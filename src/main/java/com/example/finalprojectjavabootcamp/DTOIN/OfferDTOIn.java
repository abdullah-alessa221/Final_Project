package com.example.finalprojectjavabootcamp.DTOIN;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OfferDTOIn {
    @NotNull(message = "price is required")
    @Positive(message = "price must be positive")
    private Double price;
}
