package com.example.finalprojectjavabootcamp.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Rating {

    @Id
    private Integer id;

    @Column(columnDefinition = "double not null")
    private Double rating;
    @Column(columnDefinition = "varchar(300)")
    private String review;

    @ManyToOne
    @JsonIgnore
    private Seller seller;

    @ManyToOne
    @JsonIgnore
    private Buyer buyer;

    @OneToOne
    @MapsId
    @JsonIgnore
    private Payment payment;

    @CreationTimestamp
    @Column(columnDefinition = "timestamp")
    private LocalDateTime created_at;
}
