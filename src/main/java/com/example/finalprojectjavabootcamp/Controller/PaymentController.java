package com.example.finalprojectjavabootcamp.Controller;
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
    public ResponseEntity<Payment> create(@PathVariable Integer negotiationId,
                                          @Valid @RequestBody Payment body) {
        return ResponseEntity.ok(service.create(negotiationId, body));
    }


    @PutMapping("/payments/{id}")
    public ResponseEntity<Payment> update(@PathVariable Integer id,
                                          @Valid @RequestBody Payment body) {
        return ResponseEntity.ok(service.update(id, body));
    }


    @PostMapping("/payments/{id}/confirm")
    public ResponseEntity<Payment> confirm(@PathVariable Integer id,
                                           @RequestBody(required = false) Payment body) {
        return ResponseEntity.ok(service.confirm(id, body));
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
