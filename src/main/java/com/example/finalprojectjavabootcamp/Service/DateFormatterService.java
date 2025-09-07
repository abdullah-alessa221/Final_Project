package com.example.finalprojectjavabootcamp.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class DateFormatterService {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) return "-";
        return dateTime.format(dateFormatter);
    }

    public String formatTime(LocalDateTime dateTime) {
        if (dateTime == null) return "-";
        return dateTime.format(timeFormatter);
    }
}
