package com.example.finalprojectjavabootcamp.Service;

import com.example.finalprojectjavabootcamp.Api.ApiException;
import com.example.finalprojectjavabootcamp.DTOIN.AiDTOIn;
import com.example.finalprojectjavabootcamp.Model.Buyer;
import com.example.finalprojectjavabootcamp.Model.Listing;
import com.example.finalprojectjavabootcamp.Model.Negotiation;
import com.example.finalprojectjavabootcamp.Model.NegotiationMessage;
import com.example.finalprojectjavabootcamp.Repository.BuyerRepository;
import com.example.finalprojectjavabootcamp.Repository.ListingRepository;
import com.example.finalprojectjavabootcamp.Repository.NegotiationMessageRepository;
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
    private final NegotiationMessageRepository messageRepository;
    private final AiService aiService;

    public void createManual(Integer listingId, Integer buyerId) {
        if (listingId == null) throw new ApiException("listingId is required");
        if (buyerId == null) throw new ApiException("buyerId is required");

        Listing listingRef = listingRepository.getListingById(listingId);
        if (listingRef == null) throw new ApiException("Listing not found: " + listingId);

        Buyer buyerRef = buyerRepository.findBuyersById(buyerId);
        if (buyerRef == null) throw new ApiException("Buyer not found: " + buyerId);

        Negotiation n = new Negotiation();
        n.setListing(listingRef);
        n.setBuyer(buyerRef);
        n.setSummary(null);
        n.setStatus("waiting_offer");
        n.setMode("manual");
        n.setAgreedPrice(null);

        LocalDateTime now = LocalDateTime.now();
        n.setStartedAt(now);
        n.setClosedAt(null);

        negotiationRepository.save(n);
    }


    public void enable(Integer negotiationId) {
        if (negotiationId == null) throw new ApiException("negotiationId is required");

        Negotiation n = negotiationRepository.findNegotiationById(negotiationId);
        if (n == null) throw new ApiException("Negotiation not found: " + negotiationId);

        n.setMode("ai-assisted");
        negotiationRepository.save(n);
    }

    public void disable(Integer negotiationId) {
        if (negotiationId == null) throw new ApiException("negotiationId is required");

        Negotiation n = negotiationRepository.findNegotiationById(negotiationId);
        if (n == null) throw new ApiException("Negotiation not found: " + negotiationId);

        n.setMode("manual");
        negotiationRepository.save(n);
    }


    public void createAi(Integer listingId, Integer buyerId, AiDTOIn aiDTOIn) {
        if (listingId == null) throw new ApiException("listingId is required");
        if (buyerId == null) throw new ApiException("buyerId is required");

        Listing listingRef = listingRepository.getListingById(listingId);
        if (listingRef == null) throw new ApiException("Listing not found: " + listingId);

        Buyer buyerRef = buyerRepository.findBuyersById(buyerId);
        if (buyerRef == null) throw new ApiException("Buyer not found: " + buyerId);

        // create negotiation
        Negotiation n = new Negotiation();
        n.setListing(listingRef);
        n.setBuyer(buyerRef);

        String summary = null;
        if (aiDTOIn != null) summary = aiDTOIn.getAdditionalNote();
        n.setSummary(summary);

        n.setStatus("waiting_offer");
        n.setMode("ai-assisted");
        n.setAgreedPrice(null);
        n.setStartedAt(LocalDateTime.now());
        n.setClosedAt(null);

        negotiationRepository.save(n);

        StringBuilder userMsg = new StringBuilder();
        userMsg.append("ابدأ أول رسالة تفاوضية قصيرة باسم المشتري، واسأل عن التفاصيل المؤثرة على السعر، ");
        userMsg.append("ويمكنك اقتراح عرض مبدئي مناسب دون الإفصاح عن وجود targetPrice.\n");

        if (aiDTOIn != null) {
            if (aiDTOIn.getAdditionalNote() != null) {
                userMsg.append("\n[PRIVATE additionalNote] ")
                        .append(aiDTOIn.getAdditionalNote());
            }
            if (aiDTOIn.getTargetPrice() != null) {
                userMsg.append("\n[PRIVATE targetPrice] ")
                        .append(aiDTOIn.getTargetPrice())
                        .append(" SAR");
            }
        }

        String aiText = aiService.chat("bargain_negotiation", userMsg.toString());
        if (aiText == null || aiText.trim().isEmpty()) throw new ApiException("AI returned no content");

        NegotiationMessage aiMsg = new NegotiationMessage();
        aiMsg.setNegotiation(n);
        aiMsg.setSenderType("ai");
        aiMsg.setSenderId(null);
        aiMsg.setContent(aiText.trim());
        aiMsg.setCreatedAt(LocalDateTime.now());
        messageRepository.save(aiMsg);
    }

    public void proposeOffer(Integer negotiationId, Integer buyerId, Double price) {
        if (negotiationId == null) throw new ApiException("negotiationId is required");
        if (buyerId == null) throw new ApiException("buyerId is required");
        if (price == null) throw new ApiException("price is required");
        if (price.doubleValue() <= 0) throw new ApiException("price must be positive");

        Negotiation n = negotiationRepository.findNegotiationById(negotiationId);
        if (n == null) throw new ApiException("Negotiation not found: " + negotiationId);

        if (n.getBuyer() == null) throw new ApiException("Negotiation has no buyer");
        if (!buyerId.equals(n.getBuyer().getId())) throw new ApiException("buyerId must match the negotiation buyer id");

        // ما نسمح بعرض جديد إلا إذا لسه في مرحلة انتظار عرض
        String st = n.getStatus();
        if (st == null) st = "waiting_offer";
        if (!st.equals("waiting_offer")) {
            throw new ApiException("Cannot propose offer unless status is waiting_offer");
        }

        n.setAgreedPrice(price);
        n.setStatus("waiting_acceptance");
        negotiationRepository.save(n);

        // (اختياري) نخزن رسالة في الشات تثبت العرض
        NegotiationMessage m = new NegotiationMessage();
        m.setNegotiation(n);
        m.setSenderType("buyer");
        m.setSenderId(n.getBuyer().getId());
        m.setContent("عرض المشتري: " + price + " SAR");
        m.setCreatedAt(LocalDateTime.now());
        messageRepository.save(m);
    }

    public void acceptOffer(Integer negotiationId, Integer sellerId) {
        if (negotiationId == null) throw new ApiException("negotiationId is required");
        if (sellerId == null) throw new ApiException("sellerId is required");

        Negotiation n = negotiationRepository.findNegotiationById(negotiationId);
        if (n == null) throw new ApiException("Negotiation not found: " + negotiationId);

        if (n.getListing() == null || n.getListing().getSeller() == null) {
            throw new ApiException("Listing/seller not found on negotiation");
        }
        if (!sellerId.equals(n.getListing().getSeller().getId())) {
            throw new ApiException("sellerId must match the negotiation seller id");
        }

        String st = n.getStatus();
        if (st == null || !st.equals("waiting_acceptance")) {
            throw new ApiException("Cannot accept unless status is waiting_acceptance");
        }
        if (n.getAgreedPrice() == null) {
            throw new ApiException("Cannot accept without an agreedPrice");
        }

        n.setStatus("accepted");
        n.setClosedAt(LocalDateTime.now());
        negotiationRepository.save(n);

        NegotiationMessage m = new NegotiationMessage();
        m.setNegotiation(n);
        m.setSenderType("seller");
        m.setSenderId(n.getListing().getSeller().getId());
        m.setContent("تم قبول العرض: " + n.getAgreedPrice() + " SAR");
        m.setCreatedAt(LocalDateTime.now());
        messageRepository.save(m);
    }

    public void rejectOffer(Integer negotiationId, Integer sellerId) {
        if (negotiationId == null) throw new ApiException("negotiationId is required");
        if (sellerId == null) throw new ApiException("sellerId is required");

        Negotiation n = negotiationRepository.findNegotiationById(negotiationId);
        if (n == null) throw new ApiException("Negotiation not found: " + negotiationId);

        if (n.getListing() == null || n.getListing().getSeller() == null) {
            throw new ApiException("Listing/seller not found on negotiation");
        }
        if (!sellerId.equals(n.getListing().getSeller().getId())) {
            throw new ApiException("sellerId must match the negotiation seller id");
        }

        String st = n.getStatus();
        if (st == null || !st.equals("waiting_acceptance")) {
            throw new ApiException("Cannot reject unless status is waiting_acceptance");
        }
        if (n.getAgreedPrice() == null) {
            throw new ApiException("Cannot reject without an agreedPrice");
        }

        n.setStatus("rejected");
        n.setClosedAt(LocalDateTime.now());
        negotiationRepository.save(n);

        // (اختياري) رسالة تثبت الرفض
        NegotiationMessage m = new NegotiationMessage();
        m.setNegotiation(n);
        m.setSenderType("seller");
        m.setSenderId(n.getListing().getSeller().getId());
        m.setContent("تم رفض العرض: " + n.getAgreedPrice() + " SAR");
        m.setCreatedAt(LocalDateTime.now());
        messageRepository.save(m);
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
