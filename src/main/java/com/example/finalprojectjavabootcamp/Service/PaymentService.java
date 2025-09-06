package com.example.finalprojectjavabootcamp.Service;
import com.example.finalprojectjavabootcamp.Api.ApiException;
import com.example.finalprojectjavabootcamp.Model.*;
import com.example.finalprojectjavabootcamp.Repository.NegotiationRepository;
import com.example.finalprojectjavabootcamp.Repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final NegotiationRepository negotiationRepository;


    public void create(Integer negotiationId, Payment body) {
        if (negotiationId == null) throw new ApiException("negotiationId is required");
        if (body == null) throw new ApiException("Request body is required");

        if (body.getNegotiation() != null || body.getBuyer() != null || body.getSeller() != null) {
            throw new ApiException("Do not include negotiation/buyer/seller in the request body.");
        }
        if (body.getTotalAmount() == null) throw new ApiException("totalAmount is required");
        if (body.getMethod() == null) throw new ApiException("method is required");

        Negotiation negotiation = negotiationRepository.findNegotiationById(negotiationId);
        if (negotiation == null) throw new ApiException("Negotiation not found: " + negotiationId);

        Payment existing = paymentRepository.findPaymentByNegotiation_Id(negotiationId);
        if (existing != null) throw new ApiException("Payment already exists for this negotiation");

        Buyer buyer = negotiation.getBuyer();
        if (buyer == null) throw new ApiException("Negotiation has no buyer");

        Listing listing = negotiation.getListing();
        if (listing == null) throw new ApiException("Negotiation has no listing");
        Seller seller = listing.getSeller();
        if (seller == null) throw new ApiException("Listing has no seller");

        Payment p = new Payment();
        p.setNegotiation(negotiation);
        p.setBuyer(buyer);
        p.setSeller(seller);
        p.setTotalAmount(body.getTotalAmount());

        String status = body.getStatus();
        if (status == null) status = "pending";
        p.setStatus(status);

        p.setMethod(body.getMethod());
        p.setProviderRef(body.getProviderRef());

        LocalDateTime created = body.getCreatedAt();
        if (created == null) created = LocalDateTime.now();
        p.setCreatedAt(created);

        p.setConfirmedAt(body.getConfirmedAt());
        paymentRepository.save(p);
    }

    public void update(Integer id, Payment body) {
        if (id == null) throw new ApiException("id is required for update");
        if (body == null) throw new ApiException("Request body is required");

        Payment existing = paymentRepository.findPaymentById(id);
        if (existing == null) throw new ApiException("Payment not found: " + id);

        if (body.getNegotiation() != null) throw new ApiException("Cannot change negotiation");
        if (body.getBuyer() != null) throw new ApiException("Cannot change buyer");
        if (body.getSeller() != null) throw new ApiException("Cannot change seller");
        if (body.getCreatedAt() != null) throw new ApiException("Cannot change createdAt");

        if (body.getTotalAmount() != null) existing.setTotalAmount(body.getTotalAmount());
        if (body.getMethod() != null) existing.setMethod(body.getMethod());
        if (body.getStatus() != null) existing.setStatus(body.getStatus());
        if (body.getProviderRef() != null) existing.setProviderRef(body.getProviderRef());
        if (body.getConfirmedAt() != null) existing.setConfirmedAt(body.getConfirmedAt());

        paymentRepository.save(existing);
    }

    public void confirm(Integer id, Payment body) {
        if (id == null) throw new ApiException("id is required to confirm");

        Payment existing = paymentRepository.findPaymentById(id);
        if (existing == null) throw new ApiException("Payment not found: " + id);

        existing.setStatus("confirmed");

        if (body != null && body.getProviderRef() != null) {
            existing.setProviderRef(body.getProviderRef());
        }

        LocalDateTime ts = null;
        if (body != null) ts = body.getConfirmedAt();
        if (ts == null) ts = LocalDateTime.now();
        existing.setConfirmedAt(ts);

        paymentRepository.save(existing);
    }

    public Payment findById(Integer id) {
        Payment p = paymentRepository.findPaymentById(id);
        if (p == null) throw new ApiException("Payment not found: " + id);
        return p;
    }

    public List<Payment> list(Integer buyerId, Integer sellerId, Integer negotiationId) {
        if (negotiationId != null) {
            Payment one = paymentRepository.findPaymentByNegotiation_Id(negotiationId);
            if (one == null) {
                return new ArrayList<>();
            }
            List<Payment> list = new ArrayList<>();
            list.add(one);
            return list;
        }

        if (buyerId != null && sellerId != null) {
            return paymentRepository.findAllByBuyer_IdAndSeller_Id(buyerId, sellerId);
        }

        if (buyerId != null) {
            return paymentRepository.findAllByBuyer_Id(buyerId);
        }

        if (sellerId != null) {
            return paymentRepository.findAllBySeller_Id(sellerId);
        }

        return paymentRepository.findAll();
    }
}
