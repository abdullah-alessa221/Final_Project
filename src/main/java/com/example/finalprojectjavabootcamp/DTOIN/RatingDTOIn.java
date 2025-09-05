package com.example.finalprojectjavabootcamp.DTOIN;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingDTOIn {

    @NotNull(message = "Rating is required")
    @Max(value = 5, message = "Rating must be between 0 and 5")
    @Min(value = 0, message = "Rating must be between 0 and 5")
    private Double rating;
    @Size(max = 300, message = "Review must be 300 characters at most")
    private String review;
}
