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
public class Buyer {
    @Id
    private Integer id;

    @OneToOne
    @MapsId
    @JsonIgnore
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "buyer")
    private Set<Calls> calls;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "buyer")
    private Set<Rating> ratings;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "buyer")
    private Set<Search> searches;
}
