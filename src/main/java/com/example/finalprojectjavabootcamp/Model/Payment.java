package com.example.finalprojectjavabootcamp.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Check(constraints = "status IN ('pending','confirmed','failed','cancelled')")
public class Payment {

    @Id
    private Integer id;

    @OneToOne
    @MapsId
    @JsonIgnore
    private Negotiation negotiation;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "payment" )
    @PrimaryKeyJoinColumn
    private Rating rating;

    @ManyToOne
    @JsonIgnore
    private Subscription subscription;

    @Column(columnDefinition = "Boolean not null")
    private Boolean isSubscription;

    @DecimalMin(value = "0.0", message = "totalAmount must be >= 0")
    @Column(nullable = false, columnDefinition = "decimal(19,2)")
    private Double totalAmount;

    @Column(nullable = false, columnDefinition = "varchar(20)")
    private String status;

    @Column(columnDefinition = "varchar(255)")
    private String paymentId;

    @Column(columnDefinition = "timestamp")
    @CreationTimestamp
    private LocalDateTime created_at;
}
