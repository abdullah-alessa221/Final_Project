package com.example.finalprojectjavabootcamp.DTOOUT;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class BuyerCallDTOOut {

    private Integer callId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double duration;
    private String status;
    private String sellerName;
}
