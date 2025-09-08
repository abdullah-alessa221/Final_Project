package com.example.finalprojectjavabootcamp.DTOIN;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {

    @NotEmpty(message = "cardName cannot be empty")
    private String cardName;

    @NotEmpty(message = "cardNumber cannot be empty")
    private String cardNumber;

    @NotEmpty(message = "cardCvc cannot be empty")
    private String cardCvc;

    @NotEmpty(message = "cardMonth cannot be empty")
    private String cardMonth;

    @NotEmpty(message = "cardYear cannot be null")
    private String cardYear;

}