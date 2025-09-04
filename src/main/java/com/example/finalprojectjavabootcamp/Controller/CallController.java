package com.example.finalprojectjavabootcamp.Controller;

import com.example.finalprojectjavabootcamp.Api.ApiResponse;
import com.example.finalprojectjavabootcamp.Service.CallService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/call")
@RequiredArgsConstructor
public class CallController {

    private final CallService callService;

    @PostMapping("/start/{buyerId}/{sellerId}")
    public ResponseEntity<?> startCall(@PathVariable Integer buyerId, @PathVariable Integer sellerId){
        callService.startCall(buyerId,sellerId);
        return ResponseEntity.status(200).body(new ApiResponse("Call starting"));
    }

    @PutMapping("/end/{callId}")
    public ResponseEntity<?> endCall(@PathVariable Integer callId) {
        callService.endCall(callId);
        return ResponseEntity.status(200).body(new ApiResponse("Call ended successfully"));
    }

    @GetMapping("/duration/{callId}")
    public ResponseEntity<?> getDuration(@PathVariable Integer callId) {
        Double callDurating = callService.getDuration(callId);
        return ResponseEntity.status(200).body(callDurating);
    }
    @GetMapping("/get-all")
    public ResponseEntity<?> getAllCalls(){
        return ResponseEntity.status(200).body(callService.getAllCalls());
    }

    }
