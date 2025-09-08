package com.example.finalprojectjavabootcamp.Controller;
import com.example.finalprojectjavabootcamp.Api.ApiResponse;
import com.example.finalprojectjavabootcamp.Model.Payment;
import com.example.finalprojectjavabootcamp.Service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController {
    private final PaymentService service;


    @PostMapping("/negotiations/{negotiationId}/payments")
    public ResponseEntity<?> create(@PathVariable Integer negotiationId,
                                          @Valid @RequestBody Payment body) {
        service.create(negotiationId, body);
        return ResponseEntity.ok(new ApiResponse("payment created successfully"));
    }


    @PutMapping("/payments/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,
                                          @Valid @RequestBody Payment body) {
        service.update(id, body);
        return ResponseEntity.ok(new ApiResponse("payment updated successfully"));
    }


    @PostMapping("/payments/{id}/confirm")
    public ResponseEntity<?> confirm(@PathVariable Integer id,
                                           @RequestBody(required = false) Payment body) {
        service.confirm(id, body);
        return ResponseEntity.ok(new ApiResponse("payment confirmed successfully"));
    }

    @GetMapping("/payments/{id}")
    public ResponseEntity<Payment> get(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }


    @GetMapping("/payments")
    public ResponseEntity<List<Payment>> list(@RequestParam(required = false) Integer buyerId,
                                              @RequestParam(required = false) Integer sellerId,
                                              @RequestParam(required = false) Integer negotiationId) {
        return ResponseEntity.ok(service.list(buyerId, sellerId, negotiationId));
    }
}
