package com.example.finalprojectjavabootcamp.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Rating {

    @Id
    private Integer id;

    private Double rating;
    private String review;

    // todo add the relations in the user side
    @ManyToOne
    @JsonIgnore
    private Seller seller;

    @ManyToOne
    @JsonIgnore
    private Buyer buyer;

    // private Payment payment
}
