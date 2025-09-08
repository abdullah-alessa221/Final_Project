package com.example.finalprojectjavabootcamp.Model;

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
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(255)")
    private String filePath;



    @ManyToOne
    private Buyer buyer;

    @ManyToOne
    private Seller seller;


    @CreationTimestamp
    @Column(columnDefinition = "timestamp")
    private LocalDateTime created_at;
}
