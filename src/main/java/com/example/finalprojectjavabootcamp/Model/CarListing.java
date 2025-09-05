package com.example.finalprojectjavabootcamp.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CarListing {

    @Id
    private Integer id;

    private String car_type; // suv, sedan... etc
    private Integer year;
    private Integer mileage;
    private String make;
    private String model;
    private String color;
    private String fuel_type;



    @OneToOne
    @MapsId
    @JsonIgnore
    private Listing listing;

}
