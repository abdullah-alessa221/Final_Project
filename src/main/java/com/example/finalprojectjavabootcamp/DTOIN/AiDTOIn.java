package com.example.finalprojectjavabootcamp.DTOIN;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AiDTOIn {

    @Column(columnDefinition = "decimal(19,2)")
    private Double targetPrice;

    @Size(min = 1, max = 1000, message = "additionalNote must be less than 1000 character")
    @Column(columnDefinition = "varchar(1000)")
    private String additionalNote;


}
