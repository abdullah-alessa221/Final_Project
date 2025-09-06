package com.example.finalprojectjavabootcamp.DTOOUT;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ProductDTOOut {
    private String title;
    private String description;
    private String type; // car, real_estate
    private Double price;
    private String city;

}
