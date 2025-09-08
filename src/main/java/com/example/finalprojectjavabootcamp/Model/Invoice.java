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
public class Invoice {

    @Id
    private Integer id;


    @OneToOne
    @MapsId
    @JsonIgnore
    private Payment payment;

    @Column(columnDefinition = "varchar(255)")
    private String filePath;

    @CreationTimestamp
    @Column(columnDefinition = "timestamp")
    private LocalDateTime created_at;
}
