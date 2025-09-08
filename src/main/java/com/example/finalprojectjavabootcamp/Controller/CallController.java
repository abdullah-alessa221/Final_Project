package com.example.finalprojectjavabootcamp.Controller;

import com.example.finalprojectjavabootcamp.Api.ApiResponse;
import com.example.finalprojectjavabootcamp.Service.CallService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/v1/call")
@RequiredArgsConstructor
public class CallController {

    private final CallService callService;


    @PostMapping("/sync/{sellerId}")
    public ResponseEntity<?> syncCallsFromApi(@PathVariable Integer sellerId) {
            return ResponseEntity.status(200).body(callService.retrieveAndUpdateAllCalls(sellerId));
    }


    @GetMapping("/get-all")
    public ResponseEntity<?> getAllCalls() {
        return ResponseEntity.status(200).body(callService.getAllCalls());
    }



    @GetMapping("/get-call-by-call-id/{id}")
    public ResponseEntity<?> getCallById(@PathVariable String id) {
        return ResponseEntity.status(200).body(callService.getCallById(id));
    }

    @GetMapping("/get-by-phone/{phoneNumber}")
    public ResponseEntity<?> getCallsByPhoneNumber(@PathVariable String phoneNumber) {
        return ResponseEntity.status(200).body(callService.getCallsByPhoneNumber(phoneNumber));
    }

    @GetMapping("/get-by-started-at/{startedAt}")
    public ResponseEntity<?> getCallsByStartedAt(@PathVariable String startedAt) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(startedAt, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            return ResponseEntity.status(200).body(callService.getCallsByStartedAt(dateTime));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new ApiResponse("Invalid date format. Use: yyyy-MM-ddTHH:mm:ss"));
        }
    }

    @GetMapping("/get-by-status/{status}")
    public ResponseEntity<?> getCallsByStatus(@PathVariable String status) {
        return ResponseEntity.status(200).body(callService.getCallsByStatus(status));
    }

    @GetMapping("/get-by-seller/{sellerId}")
    public ResponseEntity<?> getCallsBySeller(@PathVariable Integer sellerId) {
            return ResponseEntity.status(200).body(callService.getCallsBySeller(sellerId));
    }
    @GetMapping("/get-by-date-range")
    public ResponseEntity<?> getCallsByDateRange(@RequestParam String startDate, @RequestParam String endDate) {
        try {
            LocalDateTime start = LocalDateTime.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            LocalDateTime end = LocalDateTime.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            return ResponseEntity.status(200).body(callService.getCallsByDateRange(start, end));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new ApiResponse("Invalid date format. Use: yyyy-MM-ddTHH:mm:ss"));

    }
    }

    @GetMapping("/get-by-seller-and-status")
    public ResponseEntity<?> getCallsBySellerAndStatus(@RequestParam Integer sellerId, @RequestParam String status) {

            return ResponseEntity.status(200).body(callService.getCallsBySellerAndStatus(sellerId,status));

    }

}