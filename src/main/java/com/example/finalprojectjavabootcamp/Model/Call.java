package com.example.finalprojectjavabootcamp.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Call {
    @Id
    private String id;


    @Column(columnDefinition = "TEXT")
    private String transcriptSummary;

    @NotEmpty(message = "Customer number cannot be empty")
    private String customerNumber;

    @NotNull(message = "Cost cannot be null")
    @Positive(message = "Cost must be positive")
    private Double cost;

    @NotEmpty(message = "Start time cannot be empty")
    private LocalDateTime startedAt;

    @NotEmpty(message = "End time cannot be empty")
    private LocalDateTime endedAt;

    @NotEmpty(message = "Status cannot be empty")
    private String status;


    @ManyToOne
    @JsonIgnore
    private Seller seller;

}
