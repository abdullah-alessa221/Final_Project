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
public class SearchResults {

    @Id
    private Integer id;


    private Integer rank;


    @OneToOne
    private Listing listing;

    @ManyToOne
    @JsonIgnore
    private Search search;

}
