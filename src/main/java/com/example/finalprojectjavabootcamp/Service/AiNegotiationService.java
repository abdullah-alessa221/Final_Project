package com.example.finalprojectjavabootcamp.Service;
import com.example.finalprojectjavabootcamp.Api.ApiException;
import com.example.finalprojectjavabootcamp.Model.Listing;
import com.example.finalprojectjavabootcamp.Model.Negotiation;
import com.example.finalprojectjavabootcamp.Model.NegotiationMessage;
import com.example.finalprojectjavabootcamp.Repository.NegotiationMessageRepository;
import com.example.finalprojectjavabootcamp.Repository.NegotiationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiNegotiationService {

    private final NegotiationRepository negotiationRepository;
    private final NegotiationMessageRepository messageRepository;

    public void enable(Integer negotiationId) {
        if (negotiationId == null) throw new ApiException("negotiationId is required");

        Negotiation n = negotiationRepository.findNegotiationById(negotiationId);
        if (n == null) throw new ApiException("Negotiation not found: " + negotiationId);

        n.setMode("ai-assisted");
        negotiationRepository.save(n);
    }

}
