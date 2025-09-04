package com.example.finalprojectjavabootcamp.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Listing {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String description;
    private String type; // car, real_estate
    private String status;
    private Double price;

    @ManyToOne
    @JsonIgnore
    private Seller seller;




    @OneToOne(cascade = CascadeType.ALL, mappedBy = "listing")
    @PrimaryKeyJoinColumn
    private CarListing carListing;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "listing")
    @PrimaryKeyJoinColumn
    private RealEstateListing realEstateListing;

    @CreationTimestamp
    private String created_at;
    @UpdateTimestamp
    private String updated_at;
}
