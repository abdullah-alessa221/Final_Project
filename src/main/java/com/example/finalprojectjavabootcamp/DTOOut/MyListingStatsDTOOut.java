package com.example.finalprojectjavabootcamp.DTOOut;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MyListingStatsDTOOut {
    private Double waiting_offer;
    private Double waiting_acceptance;
    private Double accepted;
    private Double rejected;
}
