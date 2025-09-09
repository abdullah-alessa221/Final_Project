package com.example.finalprojectjavabootcamp.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
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
public class Search {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Pattern(regexp = "^(car|real_estate)$", message = "type must be car or real_estate")
    @Column(columnDefinition = "varchar(11) not null")
    private String type;

    @Column(columnDefinition = "varchar(255)")
    private String query;

    @Column(columnDefinition = "Boolean not null")
    private Boolean autoNegotiation;

    @ManyToOne
    @JsonIgnore
    private Buyer buyer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "search")
    private Set<Result> results;
}
