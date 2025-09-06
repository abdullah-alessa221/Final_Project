package com.example.finalprojectjavabootcamp.Service;

import com.example.finalprojectjavabootcamp.Api.ApiException;
import com.example.finalprojectjavabootcamp.Model.Buyer;
import com.example.finalprojectjavabootcamp.Model.Listing;
import com.example.finalprojectjavabootcamp.Model.Negotiation;
import com.example.finalprojectjavabootcamp.Repository.BuyerRepository;
import com.example.finalprojectjavabootcamp.Repository.ListingRepository;
import com.example.finalprojectjavabootcamp.Repository.NegotiationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NegotiationService {
    private final NegotiationRepository negotiationRepository;
    private final ListingRepository listingRepository;
    private final BuyerRepository buyerRepository;

    public void create(Integer listingId, Integer buyerId, Negotiation body) {
        if (listingId == null) throw new ApiException("listingId is required");
        if (buyerId == null) throw new ApiException("buyerId is required");
        if (body == null) throw new ApiException("Request body is required");
        if (body.getListing() != null || body.getBuyer() != null) {
            throw new ApiException("Do not include listing/buyer in the request body.");
        }

        Listing listingRef = listingRepository.getListingById(listingId);
        if (listingRef == null) throw new ApiException("Listing not found: " + listingId);

        Buyer buyerRef = buyerRepository.findBuyersById(buyerId);
        if (buyerRef == null) throw new ApiException("Buyer not found: " + buyerId);

        Negotiation n = new Negotiation();
        n.setListing(listingRef);
        n.setBuyer(buyerRef);
        n.setSummary(body.getSummary());

        //todo update those to the new status
        String status = body.getStatus();
        if (status == null) status = "active";
        n.setStatus(status);

        String mode = body.getMode();
        if (mode == null) mode = "manual";
        n.setMode(mode);

        n.setAgreedPrice(body.getAgreedPrice());

        LocalDateTime startedAt = body.getStartedAt();
        if (startedAt == null) startedAt = LocalDateTime.now();
        n.setStartedAt(startedAt);

        n.setClosedAt(body.getClosedAt());

        negotiationRepository.save(n);
    }

    public void update(Integer id, Negotiation body) {
        if (id == null) throw new ApiException("id is required for update");
        if (body == null) throw new ApiException("Request body is required");

        Negotiation n = negotiationRepository.findNegotiationById(id);
        if (n == null) throw new ApiException("Negotiation not found: " + id);

        if (body.getListing() != null || body.getBuyer() != null) {
            throw new ApiException("Updating listing/buyer is not allowed.");
        }

        if (body.getSummary() != null)     n.setSummary(body.getSummary());
        if (body.getStatus() != null)      n.setStatus(body.getStatus());
        if (body.getMode() != null)        n.setMode(body.getMode());
        if (body.getAgreedPrice() != null) n.setAgreedPrice(body.getAgreedPrice());
        if (body.getStartedAt() != null)   n.setStartedAt(body.getStartedAt());
        if (body.getClosedAt() != null)    n.setClosedAt(body.getClosedAt());

        negotiationRepository.save(n);
    }

    public void close(Integer id, Negotiation body) {
        if (id == null) throw new ApiException("id is required to close");

        Negotiation n = negotiationRepository.findNegotiationById(id);
        if (n == null) throw new ApiException("Negotiation not found: " + id);

        n.setStatus("closed");

        LocalDateTime ts = null;
        if (body != null) ts = body.getClosedAt();
        if (ts == null) ts = LocalDateTime.now();
        n.setClosedAt(ts);

        negotiationRepository.save(n);
    }

    public Negotiation findById(Integer id) {
        Negotiation n = negotiationRepository.findNegotiationById(id);
        if (n == null) throw new ApiException("Negotiation not found: " + id);
        return n;
    }

    public List<Negotiation> list(Integer buyerId, Integer listingId) {
        if (buyerId == null && listingId == null) {
            return negotiationRepository.findAll();
        }

        if (buyerId != null && listingId != null) {
            Negotiation one = negotiationRepository
                    .findNegotiationByListing_IdAndBuyer_Id(listingId, buyerId);
            if (one == null) {
                return new ArrayList<>();
            }
            List<Negotiation> list = new ArrayList<>();
            list.add(one);
            return list;
        }

        if (buyerId != null) {
            return negotiationRepository.findAllByBuyer_Id(buyerId);
        }

        return negotiationRepository.findAllByListing_Id(listingId);
    }
}
