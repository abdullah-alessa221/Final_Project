package com.example.finalprojectjavabootcamp.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String type;

    private Double price;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String status;

    private Integer remainingDays;

    @OneToOne
    @JsonIgnore
    private Seller seller;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subscription")
    private Set<Payment> payment;

}
