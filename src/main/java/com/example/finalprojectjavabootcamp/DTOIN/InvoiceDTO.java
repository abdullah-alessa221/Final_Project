package com.example.finalprojectjavabootcamp.DTOIN;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InvoiceDTO {

    private String id;
    private String carModel;
    private String carYear;
    private String buyerName;
    private String buyerPhone;
    private String sellerName;
    private String sellerPhone;
    private Double price;
    private String date;
    private String filePath;
}
