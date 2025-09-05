package com.example.finalprojectjavabootcamp.Controller;

import com.example.finalprojectjavabootcamp.Model.Negotiation;
import com.example.finalprojectjavabootcamp.Service.NegotiationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/negotiations")
public class NegotiationController {

    private final NegotiationService service;

    @PostMapping("/{listingId}/{buyerId}")
    public ResponseEntity<?> create(@PathVariable Integer listingId,
                                              @PathVariable Integer buyerId,
                                              @Valid @RequestBody Negotiation body) {
        return ResponseEntity.ok(service.create(listingId, buyerId, body));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Negotiation> update(@PathVariable Integer id,
                                              @Valid @RequestBody Negotiation body) {
        return ResponseEntity.ok(service.update(id, body));
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<?> close(@PathVariable Integer id,
                                             @RequestBody(required = false) Negotiation body) {
        return ResponseEntity.ok(service.close(id, body));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> get(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("get/list")
    public ResponseEntity<?> list(@RequestParam(required = false) Integer buyerId,
                                                  @RequestParam(required = false) Integer listingId) {
        return ResponseEntity.ok(service.list(buyerId, listingId));
    }
}
