package com.example.finalprojectjavabootcamp.Service;

import com.example.finalprojectjavabootcamp.Api.ApiException;
import com.example.finalprojectjavabootcamp.Model.Negotiation;
import com.example.finalprojectjavabootcamp.Model.NegotiationMessage;
import com.example.finalprojectjavabootcamp.Repository.NegotiationMessageRepository;
import com.example.finalprojectjavabootcamp.Repository.NegotiationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NegotiationMessageService {
    private final NegotiationMessageRepository messageRepository;
    private final NegotiationRepository negotiationRepository;

    public void create(Integer negotiationId, NegotiationMessage body) {
        if (negotiationId == null) throw new ApiException("negotiationId is required");
        if (body == null) throw new ApiException("Request body is required");

        if (body.getNegotiation() != null) {
            throw new ApiException("Do not include negotiation object in the request body.");
        }

        String st = body.getSenderType();
        if (st == null) throw new ApiException("senderType is required");
        if (!"buyer".equals(st) && !"seller".equals(st) && !"ai".equals(st)) {
            throw new ApiException("senderType must be buyer, seller, or ai");
        }
        if ("ai".equals(st)) {
            if (body.getSenderId() != null) {
                throw new ApiException("senderId must be null when senderType is ai");
            }
        } else {
            if (body.getSenderId() == null) {
                throw new ApiException("senderId is required when senderType is buyer or seller");
            }
        }

        Negotiation negotiation = negotiationRepository.findNegotiationById(negotiationId);
        if (negotiation == null) throw new ApiException("Negotiation not found: " + negotiationId);

        NegotiationMessage m = new NegotiationMessage();
        m.setNegotiation(negotiation);
        m.setSenderType(st);
        m.setSenderId(body.getSenderId());
        m.setContent(body.getContent());

        LocalDateTime ts = body.getCreatedAt();
        if (ts == null) ts = LocalDateTime.now();
        m.setCreatedAt(ts);

        messageRepository.save(m);
    }

    public void update(Integer id, NegotiationMessage body) {
        if (id == null) throw new ApiException("id is required for update");
        if (body == null) throw new ApiException("Request body is required");

        NegotiationMessage existing = messageRepository.findNegotiationMessageById(id);
        if (existing == null) throw new ApiException("NegotiationMessage not found: " + id);

        if (body.getNegotiation() != null) throw new ApiException("Cannot change negotiation");
        if (body.getSenderType() != null) throw new ApiException("Cannot change senderType");
        if (body.getSenderId() != null) throw new ApiException("Cannot change senderId");
        if (body.getCreatedAt() != null) throw new ApiException("Cannot change createdAt");

        if (body.getContent() != null) existing.setContent(body.getContent());

        messageRepository.save(existing);
    }

    public NegotiationMessage findById(Integer id) {
        NegotiationMessage m = messageRepository.findNegotiationMessageById(id);
        if (m == null) throw new ApiException("NegotiationMessage not found: " + id);
        return m;
    }

    public List<NegotiationMessage> listAsc(Integer negotiationId) {
        if (negotiationId == null) throw new ApiException("negotiationId is required");
        // ensure negotiation exists (optional but clearer errors)
        Negotiation n = negotiationRepository.findNegotiationById(negotiationId);
        if (n == null) throw new ApiException("Negotiation not found: " + negotiationId);

        return messageRepository.findAllByNegotiation_IdOrderByCreatedAtAsc(negotiationId);
    }

    public List<NegotiationMessage> listDesc(Integer negotiationId) {
        if (negotiationId == null) throw new ApiException("negotiationId is required");
        Negotiation n = negotiationRepository.findNegotiationById(negotiationId);
        if (n == null) throw new ApiException("Negotiation not found: " + negotiationId);

        return messageRepository.findAllByNegotiation_IdOrderByCreatedAtDesc(negotiationId);
    }
}
