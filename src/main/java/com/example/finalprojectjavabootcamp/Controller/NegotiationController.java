package com.example.finalprojectjavabootcamp.Controller;

import com.example.finalprojectjavabootcamp.Api.ApiResponse;
import com.example.finalprojectjavabootcamp.DTOIN.AiDTOIn;
import com.example.finalprojectjavabootcamp.DTOIN.OfferDTOIn;
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

    @PostMapping("/{listingId}/{buyerId}/manual")
    public ResponseEntity<?> createManual(@PathVariable Integer listingId,
                                          @PathVariable Integer buyerId) {
        service.createManual(listingId, buyerId);
        return ResponseEntity.ok(new ApiResponse("negotiation (manual) created successfully"));
    }

    @PostMapping("/{listingId}/{buyerId}/ai")
    public ResponseEntity<?> createAi(@PathVariable Integer listingId,
                                      @PathVariable Integer buyerId,
                                      @Valid @RequestBody(required = false) AiDTOIn aiDTOIn) {
        service.createAi(listingId, buyerId, aiDTOIn);
        return ResponseEntity.ok(new ApiResponse("negotiation (ai) created and AI started the chat"));
    }

    @PostMapping("/{id}/offer/{buyerId}")
    public ResponseEntity<?> propose(@PathVariable Integer id,
                                     @PathVariable Integer buyerId,
                                     @Valid @RequestBody OfferDTOIn body) {
        service.proposeOffer(id, buyerId, body.getPrice());
        return ResponseEntity.ok(new ApiResponse("offer submitted, waiting for seller acceptance"));
    }

    @PutMapping("/{id}/accept/{sellerId}")
    public ResponseEntity<?> accept(@PathVariable Integer id,
                                    @PathVariable Integer sellerId) {
        service.acceptOffer(id, sellerId);
        return ResponseEntity.ok(new ApiResponse("offer accepted"));
    }

    @PutMapping("/{id}/reject/{sellerId}")
    public ResponseEntity<?> reject(@PathVariable Integer id,
                                    @PathVariable Integer sellerId) {
        service.rejectOffer(id, sellerId);
        return ResponseEntity.ok(new ApiResponse("offer rejected"));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> get(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("get/list/{listingId}/buyer/{buyerId}")
    public ResponseEntity<?> list(@PathVariable Integer buyerId,
                                                  @PathVariable Integer listingId) {
        return ResponseEntity.ok(service.list(buyerId, listingId));
    }
}
