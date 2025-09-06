package com.example.finalprojectjavabootcamp.DTOOUT;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTOOut {
    private Integer id;
    private String name;
    private String email;
    private String phone;
    private String location;
    private String role;
    private String status;
}
