package com.example.finalprojectjavabootcamp.DTOOUT;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SellerDTOOut {

    private Integer id;

    private String name;

    private String email;

    private String location;

    private String phone;

    private String description;

    private String preferredContact;

    private String status;
}
