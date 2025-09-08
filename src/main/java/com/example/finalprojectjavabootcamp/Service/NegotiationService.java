package com.example.finalprojectjavabootcamp.Service;

import com.example.finalprojectjavabootcamp.Api.ApiException;
import com.example.finalprojectjavabootcamp.DTOIN.AiDTOIn;
import com.example.finalprojectjavabootcamp.Model.*;
import com.example.finalprojectjavabootcamp.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
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

    public void createAi(Integer listingId, Integer buyerId, AiDTOIn aiDTOIn) {

        Listing listingRef = listingRepository.getListingById(listingId);
        if (listingRef == null) throw new ApiException("Listing not found: " + listingId);

        Buyer buyerRef = buyerRepository.findBuyersById(buyerId);
        if (buyerRef == null) throw new ApiException("Buyer not found: " + buyerId);

        Negotiation n = new Negotiation();
        n.setListing(listingRef);
        n.setBuyer(buyerRef);
        n.setSummary(aiDTOIn != null ? aiDTOIn.getAdditionalNote() : null);
        n.setStatus("waiting_offer");
        n.setMode("ai-assisted");
        n.setAgreedPrice(null);
        n.setStartedAt(LocalDateTime.now());
        n.setClosedAt(null);
        negotiationRepository.save(n);

        StringBuilder userMsg = new StringBuilder();

        userMsg.append("بيانات الإعلان:\n");
        appendListingBlock(userMsg, listingRef);

        userMsg.append("\nسجل المحادثة كاملًا (من الأقدم إلى الأحدث):\n");
        userMsg.append("لا يوجد حتى الآن.\n");

        if (aiDTOIn != null) {
            if (aiDTOIn.getAdditionalNote() != null) {
                userMsg.append("\n[PRIVATE additionalNote] ").append(aiDTOIn.getAdditionalNote());
            }
            if (aiDTOIn.getTargetPrice() != null) {
                userMsg.append("\n[PRIVATE targetPrice] ").append(aiDTOIn.getTargetPrice()).append(" SAR");
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

    private void appendListingBlock(StringBuilder sb, Listing listing) {
        sb.append("- العنوان: ").append(show(listing.getTitle())).append("\n");
        sb.append("- النوع: ").append(show(listing.getType())).append("\n");
        sb.append("- الحالة (الإعلان): ").append(show(listing.getStatus())).append("\n");
        sb.append("- المدينة: ").append(show(listing.getCity())).append("\n");
        sb.append("- أقل سعر معلَن (SAR): ").append(show(listing.getLeast_price())).append("\n");
        sb.append("- الوصف: ").append(show(listing.getDescription())).append("\n");

        String t = listing.getType() == null ? "" : listing.getType().toLowerCase();

        if ("car".equals(t) && listing.getCarListing() != null) {
            Object car = listing.getCarListing();
            sb.append("\n[تفاصيل السيارة]\n");
            sb.append("  الماركة/الموديل: ").append(show(tryGetStr(car, "getMake"))).append(" / ")
                    .append(show(tryGetStr(car, "getModel"))).append("\n");
            sb.append("  السنة: ").append(show(tryGetStr(car, "getYear"))).append("\n");
            sb.append("  العداد: ").append(show(tryGetStr(car, "getMileage"))).append("\n");
            sb.append("  اللون: ").append(show(tryGetStr(car, "getColor"))).append("\n");
            sb.append("  الوقود: ").append(show(tryGetStr(car, "getFuelType", "getFuel_type"))).append("\n");
            sb.append("  السعر المطلوب (SAR): ").append(show(tryGetStr(car, "getPrice"))).append("\n");
            sb.append("  وصف إضافي: ").append(show(tryGetStr(car, "getDescription"))).append("\n");

        } else if ("real_estate".equals(t) && listing.getRealEstateListing() != null) {
            Object re = listing.getRealEstateListing();
            sb.append("\n[تفاصيل العقار]\n");
            sb.append("  الموقع: ").append(show(tryGetStr(re, "getLocation", "getCity"))).append("\n");
            sb.append("  المساحة (م²): ").append(show(tryGetStr(re, "getArea", "getSquareMeter"))).append("\n");
            sb.append("  السعر المطلوب (SAR): ").append(show(tryGetStr(re, "getPrice", "getLeast_price"))).append("\n");
            sb.append("  نوع العقار: ").append(show(tryGetStr(re, "getType"))).append("\n");
            sb.append("  إيجار؟ ").append(show(tryGetStr(re, "getIsRental"))).append("\n");
            sb.append("  الغرف/الدورات: ").append(show(tryGetStr(re, "getRooms"))).append(" / ")
                    .append(show(tryGetStr(re, "getBathrooms"))).append("\n");
            sb.append("  الحي: ").append(show(tryGetStr(re, "getNeighborhood"))).append("\n");
            sb.append("  وصف إضافي: ").append(show(tryGetStr(re, "getDescription"))).append("\n");
        }
    }


    private static String show(Object v) {
        return v == null ? "-" : String.valueOf(v);
    }

    private String tryGetStr(Object obj, String... getters) {
        if (obj == null || getters == null) return null;
        int i = 0;
        while (i < getters.length) {
            String g = getters[i];
            try {
                java.lang.reflect.Method m = obj.getClass().getMethod(g);
                Object val = m.invoke(obj);
                if (val != null) return String.valueOf(val);
            } catch (Exception ignored) {}
            i = i + 1;
        }
        return null;
    }



    public void proposeOffer(Integer negotiationId, Integer buyerId, Double price) {

        Negotiation n = negotiationRepository.findNegotiationById(negotiationId);
        if (n == null) throw new ApiException("Negotiation not found: " + negotiationId);

        if (n.getBuyer() == null) throw new ApiException("Negotiation has no buyer");
        if (!buyerId.equals(n.getBuyer().getId())) throw new ApiException("buyerId must match the negotiation buyer id");

        String st = n.getStatus();
        if (st == null) st = "waiting_offer";
        if (!st.equals("waiting_offer")) {
            throw new ApiException("Cannot propose offer unless status is waiting_offer");
        }

        n.setAgreedPrice(price);
        n.setStatus("waiting_acceptance");
        negotiationRepository.save(n);

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


    private String nvl(Object v) {
        return v == null ? "-" : String.valueOf(v);
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
