package com.example.finalprojectjavabootcamp.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Seller {
    @Id
    private Integer id;


    private String description;

    private String preferredContact;

    @OneToOne
    @MapsId
    @JsonIgnore
    private User user;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "seller")
    @PrimaryKeyJoinColumn
    private Subscription subscription;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seller")
    private Set<Calls> calls;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seller")
    private Set<Listing> listings;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seller")
    private Set<Rating> ratings;
}
