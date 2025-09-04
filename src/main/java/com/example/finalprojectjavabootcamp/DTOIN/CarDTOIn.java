package com.example.finalprojectjavabootcamp.DTOIN;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarDTOIn {

    @NotEmpty(message = "Title is required")
    @Size(min = 3, max = 30, message = "Title must be between 3 and 30 characters")
    private String title;
    @NotEmpty(message = "Description is required")
    @Size(min = 10, max = 300, message = "Description must be between 10 and 300 characters")
    private String description;

    @NotEmpty(message = "Car type is required")
    private String make;
    @NotEmpty(message = "Car type is required")
    private String model;

    @NotNull(message = "Year is required")
    private Integer year;

    @NotEmpty(message = "Color is required")
    @Pattern(regexp = "(?i)^(White|Black|Gray|Silver|Blue|Green|Red|Orange|Yellow|Brown|Beige|Purple|Pink|Gold|Other)$",message = "Color must be White, Black, Gray, Silver, Blue, Green, Red, Orange, Yellow, Brown, Beige, Purple, Pink, Gold or Other ")
    private String color;

    @NotEmpty(message = "Fuel type is required")
    @Pattern(regexp = "(?i)^(91|95|Diesel|Electric)$",message = "Fuel type must be 91, 95, Diesel or Electric")
    private String fuel_type;

    @PositiveOrZero(message = "Mileage must be a positive or zero number")
    @NotNull(message = "Mileage is required")
    private Integer mileage;

    @Positive(message = "Least price must be a positive number")
    private Double least_price;

    @NotEmpty(message = "City is required")
    private String city;
}
