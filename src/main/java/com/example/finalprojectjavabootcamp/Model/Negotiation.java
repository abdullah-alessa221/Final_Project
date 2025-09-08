package com.example.finalprojectjavabootcamp.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Check(constraints = "LOWER(status) IN ('waiting_offer','waiting_acceptance','accepted','rejected') AND mode IN ('manual','ai-assisted')")
public class Negotiation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JsonIgnore
    private Listing listing;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "negotiation")
    @PrimaryKeyJoinColumn
    private Payment payment;

    @ManyToOne
    @JsonIgnore
    private Buyer buyer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "negotiation")
    private Set<NegotiationMessage> negotiationMessage;

    @Size(max = 1000)
    @Column(length = 1000, columnDefinition = "varchar(1000)")
    private String summary;

    @NotBlank
    @Pattern(regexp = "^(?i)(waiting_offer|waiting_acceptance|accepted|rejected)$",
            message = "status must be waiting_offer, waiting_acceptance, accepted or rejected")
    @Column(nullable = false, length = 20, columnDefinition = "varchar(20)")
    private String status;

    @NotBlank
    @Pattern(regexp = "^(manual|ai-assisted)$",
            message = "mode must be one of: manual, ai-assisted")
    @Column(nullable = false, length = 20, columnDefinition = "varchar(20)")
    private String mode;

    @Column( columnDefinition = "decimal(19,2)")
    private Double agreedPrice;

    @Column(nullable = false, columnDefinition = "timestamp")
    private LocalDateTime startedAt;

    @Column(columnDefinition = "timestamp")
    private LocalDateTime closedAt;
}
