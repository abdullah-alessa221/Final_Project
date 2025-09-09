package com.example.finalprojectjavabootcamp.Controller;

import com.example.finalprojectjavabootcamp.Api.ApiResponse;
import com.example.finalprojectjavabootcamp.Service.CallLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/v1/call")
@RequiredArgsConstructor
public class CallLogController {

    private final CallLogService callService;


    @PostMapping("/sync/{sellerId}")
    public ResponseEntity<?> syncCallLogsFromApi(@PathVariable Integer sellerId) {
            return ResponseEntity.status(200).body(callService.retrieveAndUpdateAllCallLogs(sellerId));
    }


    @GetMapping("/get-all")
    public ResponseEntity<?> getAllCallLogs() {
        return ResponseEntity.status(200).body(callService.getAllCallLogs());
    }



    @GetMapping("/get-call-by-call-id/{id}")
    public ResponseEntity<?> getCallLogById(@PathVariable String id) {
        return ResponseEntity.status(200).body(callService.getCallLogById(id));
    }

    @GetMapping("/get-by-phone/{phoneNumber}")
    public ResponseEntity<?> getCallLogsByPhoneNumber(@PathVariable String phoneNumber) {
        return ResponseEntity.status(200).body(callService.getCallLogsByPhoneNumber(phoneNumber));
    }

    @GetMapping("/get-by-started-at/{startedAt}")
    public ResponseEntity<?> getCallLogsByStartedAt(@PathVariable String startedAt) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(startedAt, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            return ResponseEntity.status(200).body(callService.getCallLogsByStartedAt(dateTime));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new ApiResponse("Invalid date format. Use: yyyy-MM-ddTHH:mm:ss"));
        }
    }

    @GetMapping("/get-by-status/{status}")
    public ResponseEntity<?> getCallLogsByStatus(@PathVariable String status) {
        return ResponseEntity.status(200).body(callService.getCallLogsByStatus(status));
    }

    @GetMapping("/get-by-seller/{sellerId}")
    public ResponseEntity<?> getCallLogsBySeller(@PathVariable Integer sellerId) {
            return ResponseEntity.status(200).body(callService.getCallLogsBySeller(sellerId));
    }
    @GetMapping("/get-by-date-range")
    public ResponseEntity<?> getCallLogsByDateRange(@RequestParam String startDate, @RequestParam String endDate) {
        try {
            LocalDateTime start = LocalDateTime.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            LocalDateTime end = LocalDateTime.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            return ResponseEntity.status(200).body(callService.getCallLogsByDateRange(start, end));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new ApiResponse("Invalid date format. Use: yyyy-MM-ddTHH:mm:ss"));
        }
    }

    @GetMapping("/get-by-seller-and-status")
    public ResponseEntity<?> getCallLogsBySellerAndStatus(@RequestParam Integer sellerId, @RequestParam String status) {

            return ResponseEntity.status(200).body(callService.getCallLogsBySellerAndStatus(sellerId,status));

    }

}