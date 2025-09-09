package com.example.finalprojectjavabootcamp.DTOIN;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InvoiceDTO {

    private Integer id;          // رقم الفاتورة
    private String buyerName;    // اسم المشتري
    private String buyerPhone;   // هاتف المشتري
    private String sellerName;   // اسم البائع
    private String sellerPhone;  // هاتف البائع
    private String carModel;     // السيارة
    private String carYear;      // سنة الصنع
    private Double price;        // السعر
    private String date;         // التاريخ
    private String filePath;
}
