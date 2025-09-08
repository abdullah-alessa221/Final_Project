package com.example.finalprojectjavabootcamp.DTOIN;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NegotiationMessageDTOIn {

    @NotNull(message = "senderId is required")
    @Column(columnDefinition = "integer not null")
    private Integer senderId;

    @NotBlank(message = "content is required")
    @Size(max = 4000, message = "content must be at most 4000 characters")
    @Column(nullable = false, columnDefinition = "text not null")
    private String content;
}
