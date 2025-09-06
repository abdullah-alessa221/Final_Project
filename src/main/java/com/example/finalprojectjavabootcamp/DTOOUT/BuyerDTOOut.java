package com.example.finalprojectjavabootcamp.DTOOUT;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BuyerDTOOut {

    private Integer id;

    private String name;

    private String email;

    private String location;

    private String phone;

    private String status;


}
