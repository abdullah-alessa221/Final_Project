package com.example.finalprojectjavabootcamp.Controller;

import com.example.finalprojectjavabootcamp.Api.ApiResponse;
import com.example.finalprojectjavabootcamp.DTOIN.AiDTOIn;
import com.example.finalprojectjavabootcamp.DTOIN.OfferDTOIn;
import com.example.finalprojectjavabootcamp.Model.User;
import com.example.finalprojectjavabootcamp.Service.NegotiationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/negotiations")
public class NegotiationController {

    private final NegotiationService service;

    @PostMapping("/{listingId}/manual")
    public ResponseEntity<?> createManual(@PathVariable Integer listingId,
                                          @AuthenticationPrincipal User user) {
        service.createManual(listingId, user.getId());
        return ResponseEntity.ok(new ApiResponse("negotiation (manual) created successfully"));
    }

    @PostMapping("/{listingId}/ai")
    public ResponseEntity<?> createAi(@PathVariable Integer listingId,
                                      @AuthenticationPrincipal User user,
                                      @Valid @RequestBody(required = false) AiDTOIn aiDTOIn) {
        service.createAi(listingId, user.getId(), aiDTOIn);
        return ResponseEntity.ok(new ApiResponse("negotiation (ai) created and AI started the chat"));
    }

    @PostMapping("/{id}/offer")
    public ResponseEntity<?> propose(@PathVariable Integer id,
                                     @AuthenticationPrincipal User user,
                                     @Valid @RequestBody OfferDTOIn body) {
        service.proposeOffer(id, user.getId(), body.getPrice());
        return ResponseEntity.ok(new ApiResponse("offer submitted, waiting for seller acceptance"));
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<?> accept(@PathVariable Integer id,
                                    @AuthenticationPrincipal User user) {
        service.acceptOffer(id, user.getId());
        return ResponseEntity.ok(new ApiResponse("offer accepted"));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<?> reject(@PathVariable Integer id,
                                    @AuthenticationPrincipal User user) {
        service.rejectOffer(id, user.getId());
        return ResponseEntity.ok(new ApiResponse("offer rejected"));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> get(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("get/list/{listingId}/buyer")
    public ResponseEntity<?> list(@AuthenticationPrincipal User user,
                                                  @PathVariable Integer listingId) {
        return ResponseEntity.ok(service.list(user.getId(), listingId));
    }

    @PutMapping("/{negotiationId}/ai/disable")
    public ResponseEntity<?> disable(@PathVariable Integer negotiationId) {
        service.disable(negotiationId);
        return ResponseEntity.ok(new ApiResponse("Negotiation AI has been disable successfully"));
    }

    @PutMapping("/{negotiationId}/ai/enable")
    public ResponseEntity<?> enable(@PathVariable Integer negotiationId) {
        service.enable(negotiationId);
        return ResponseEntity.ok(new ApiResponse("Negotiation AI has been enabled successfully"));
    }
}
