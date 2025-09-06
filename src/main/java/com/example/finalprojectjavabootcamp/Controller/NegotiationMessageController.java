package com.example.finalprojectjavabootcamp.Controller;

import com.example.finalprojectjavabootcamp.Api.ApiResponse;
import com.example.finalprojectjavabootcamp.Model.NegotiationMessage;
import com.example.finalprojectjavabootcamp.Service.NegotiationMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/negotiation-message")
public class NegotiationMessageController {


    private final NegotiationMessageService service;


    @PostMapping("/negotiations/{negotiationId}/messages")
    public ResponseEntity<?> create(@PathVariable Integer negotiationId,
                                                     @Valid @RequestBody NegotiationMessage body) {
        service.create(negotiationId, body);
        return ResponseEntity.ok(new ApiResponse("message created successfully"));
    }


    @PatchMapping("/negotiations/messages/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,
                                                     @Valid @RequestBody NegotiationMessage body) {
        service.update(id, body);
        return ResponseEntity.ok(new ApiResponse("message updated successfully"));
    }


    @GetMapping("/negotiations/messages/{id}")
    public ResponseEntity<NegotiationMessage> get(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }


    @GetMapping("/negotiations/{negotiationId}/messages")
    public ResponseEntity<List<NegotiationMessage>> listAsc(@PathVariable Integer negotiationId) {
        return ResponseEntity.ok(service.listAsc(negotiationId));
    }


    @GetMapping("/negotiations/{negotiationId}/messages/desc")
    public ResponseEntity<List<NegotiationMessage>> listDesc(@PathVariable Integer negotiationId) {
        return ResponseEntity.ok(service.listDesc(negotiationId));
    }
}
