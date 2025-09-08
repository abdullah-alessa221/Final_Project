package com.example.finalprojectjavabootcamp.Controller;

import com.example.finalprojectjavabootcamp.Api.ApiResponse;
import com.example.finalprojectjavabootcamp.Model.Negotiation;
import com.example.finalprojectjavabootcamp.Model.NegotiationMessage;
import com.example.finalprojectjavabootcamp.Service.AiNegotiationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ai-negotiations")
public class AiNegotiationController {
    private final AiNegotiationService service;


    @PostMapping("/{negotiationId}/ai/enable")
    public ResponseEntity<?> enable(@PathVariable Integer negotiationId) {
        service.enable(negotiationId);
        return ResponseEntity.ok(new ApiResponse("Negotiation AI has been enabled successfully"));
    }


}
