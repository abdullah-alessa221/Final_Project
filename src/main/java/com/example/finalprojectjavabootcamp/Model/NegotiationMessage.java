package com.example.finalprojectjavabootcamp.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Check(constraints =
        "sender_type IN ('buyer','seller','ai') " +
                "AND ( (sender_type = 'ai' AND sender_id IS NULL) " +
                "   OR (sender_type IN ('buyer','seller') AND sender_id IS NOT NULL) )")
public class NegotiationMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JsonIgnore
    private Negotiation negotiation;

    @Pattern(regexp = "^(buyer|seller|ai)$", message = "senderType must be buyer, seller, or ai")
    @Column(nullable = false, length = 10, columnDefinition = "varchar(10)")
    private String senderType;

    // nullable if AI
    @Column(columnDefinition = "integer")
    private Integer senderId;

    @NotBlank(message = "content is required")
    @Size(max = 4000, message = "content must be at most 4000 characters")
    @Column(nullable = false, columnDefinition = "text")
    private String content;

    @Column(nullable = false, columnDefinition = "timestamp")
    private LocalDateTime createdAt;
}
