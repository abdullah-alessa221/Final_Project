package com.example.finalprojectjavabootcamp.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Check(constraints = "status IN ('pending','confirmed','failed','cancelled') AND method IN ('credit_card','sadad')")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne()
    private Negotiation negotiation;

    @ManyToOne()
    private Buyer buyer;

    @ManyToOne()
    private Seller seller;

    @DecimalMin(value = "0.0", message = "totalAmount must be >= 0")
    @Column(nullable = false, columnDefinition = "decimal(19,2)")
    private Double totalAmount;

    @Pattern(regexp = "^(pending|confirmed|failed|cancelled)$",
            message = "status must be pending, confirmed, failed or cancelled")
    @Column(nullable = false, columnDefinition = "varchar(20)")
    private String status;

    @Pattern(regexp = "^(credit_card|sadad)$", message = "method must be credit_card or sadad")
    @Column(nullable = false, length = 20, columnDefinition = "varchar(20)")
    private String method;

    @Size(max = 128, message = "providerRef must be at most 128 characters")
    @Column(length = 128, columnDefinition = "varchar(128)")
    private String providerRef;

    @Column(nullable = false, columnDefinition = "timestamp")
    private LocalDateTime createdAt;

    @Column(columnDefinition = "timestamp")
    private LocalDateTime confirmedAt;
}
