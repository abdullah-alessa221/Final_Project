package com.example.finalprojectjavabootcamp.DTOOUT;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CarInvoiceDTOOut {

    private Integer id;
    private String buyerName;
    private String buyerPhone;
    private String sellerName;
    private String sellerPhone;
    private String carModel;
    private String carYear;
    private Double price;
    private String date;
}
