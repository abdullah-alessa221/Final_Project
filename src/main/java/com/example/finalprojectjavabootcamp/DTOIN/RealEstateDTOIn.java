package com.example.finalprojectjavabootcamp.DTOIN;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RealEstateDTOIn {

    @NotEmpty(message = "Title is required")
    @Size(min = 3, max = 30, message = "Title must be between 3 and 30 characters")
    private String title;
    @NotEmpty(message = "Description is required")
    @Size(min = 10, max = 300, message = "Description must be between 10 and 300 characters")
    private String description;
    @NotEmpty(message = "Real estate type is required")
    private String type;
    @Pattern(regexp = "(?i)^(Apartment|Villa|Townhouse|Duplex|Studio|Penthouse|Store|Farm|Land|Other)$", message = "Real estate type must be Apartment, Villa, Townhouse, Duplex, Studio, Penthouse, Store, Farm, Land or Other")
    @NotNull(message = "specifying if the real estate is a renal or not is required")
    private Boolean isRental;
    @Positive(message = "Rooms must be positive")
    private Integer rooms;
    @PositiveOrZero(message = "Bathrooms must be zero or positive")
    private Integer bathrooms;
    @Positive(message = "Square meter must be positive")
    @NotNull(message = "Square meter is required")
    private Integer squareMeter;
    @NotEmpty(message = "City is required")
    private String city;
    @NotEmpty(message = "Neighborhood is required")
    private String neighborhood;
    private Double least_price;
}
