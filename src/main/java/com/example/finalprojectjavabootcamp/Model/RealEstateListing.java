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
public class RealEstateListing {

    @Id
    private Integer id;

    private String real_estate_type; // land, apartment...etc
    private Boolean for_rent;
    private Integer rooms;
    private Integer bathrooms;
    private Integer squareMeter;
    private String city;
    private String neighborhood;



    @OneToOne
    @MapsId
    @JsonIgnore
    private Listing listing;
}
