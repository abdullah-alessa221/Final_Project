package com.example.finalprojectjavabootcamp.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
//@Check(constraints = "link IS NULL OR link LIKE 'http%'")
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    private Negotiation negotiation;

    @NotNull(message = "scheduledAt is required")
    @Column(nullable = false, columnDefinition = "timestamp")
    private LocalDateTime scheduledAt;

//    @Size(max = 512, message = "link must be at most 512 characters")
//    @Pattern(regexp = "^(|https?://).*$", message = "link must start with http:// or https:// if provided")
//    @Column(name = "link", length = 512, columnDefinition = "varchar(512)")
//    private String link;
}
