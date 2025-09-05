package com.example.finalprojectjavabootcamp.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Check(constraints = "LOWER(real_estate_type) IN ('apartment','villa','townhouse','duplex', 'studio', 'penthouse', 'store', 'farm', 'land', 'other')")
public class RealEstateListing {

    @Id
    private Integer id;
    @Column(columnDefinition = "varchar(30) not null")
    private String real_estate_type;
    @Column(columnDefinition = "Boolean not null")
    private Boolean isRental;
    @Column(columnDefinition = "Integer")
    private Integer rooms;
    @Column(columnDefinition = "Integer")
    private Integer bathrooms;
    @Column(columnDefinition = "Integer not null")
    private Integer squareMeter;
    @Column(columnDefinition = "varchar(30) not null")
    private String city;
    @Column(columnDefinition = "varchar(30) not null")
    private String neighborhood;



    @OneToOne
    @MapsId
    @JsonIgnore
    private Listing listing;
}
