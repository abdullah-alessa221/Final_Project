package com.example.finalprojectjavabootcamp.DTOOUT;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ChatLineDTOOut {
    private String sender;
    private Integer senderId;
    private String content;
    private LocalDateTime createdAt;
}