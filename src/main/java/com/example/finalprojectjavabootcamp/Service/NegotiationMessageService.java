package com.example.finalprojectjavabootcamp.Service;

import com.example.finalprojectjavabootcamp.Api.ApiException;
import com.example.finalprojectjavabootcamp.DTOIN.NegotiationMessageDTOIn;
import com.example.finalprojectjavabootcamp.DTOOUT.ChatLineDTOOut;
import com.example.finalprojectjavabootcamp.Model.Listing;
import com.example.finalprojectjavabootcamp.Model.Negotiation;
import com.example.finalprojectjavabootcamp.Model.NegotiationMessage;
import com.example.finalprojectjavabootcamp.Repository.NegotiationMessageRepository;
import com.example.finalprojectjavabootcamp.Repository.NegotiationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NegotiationMessageService {
    private final NegotiationMessageRepository messageRepository;
    private final NegotiationRepository negotiationRepository;
    private final AiService aiService;


    public void sendByBuyer(Integer negotiationId, NegotiationMessageDTOIn body) {

        Negotiation n = negotiationRepository.findNegotiationById(negotiationId);
        if (n == null) throw new ApiException("Negotiation not found: " + negotiationId);

        if (n.getBuyer() == null) throw new ApiException("Negotiation has no buyer");
        if (!body.getSenderId().equals(n.getBuyer().getId())) {
            throw new ApiException("senderId must match the negotiation buyer id");
        }

        String content = body.getContent();
        NegotiationMessage m = new NegotiationMessage();
        m.setNegotiation(n);
        m.setSenderType("buyer");
        m.setSenderId(body.getSenderId());
        m.setContent(content);
        m.setCreatedAt(LocalDateTime.now());
        messageRepository.save(m);

        n.setMode("manual");
        negotiationRepository.save(n);
    }

    public void sendBySeller(Integer negotiationId, NegotiationMessageDTOIn body) {
        if (negotiationId == null) throw new ApiException("negotiationId is required");
        if (body == null) throw new ApiException("Request body is required");
        if (body.getSenderId() == null) throw new ApiException("senderId is required");
        if (body.getContent() == null || body.getContent().trim().isEmpty()) {
            throw new ApiException("content is required");
        }

        Negotiation n = negotiationRepository.findNegotiationById(negotiationId);
        if (n == null) throw new ApiException("Negotiation not found: " + negotiationId);
        if (n.getListing() == null || n.getListing().getSeller() == null) {
            throw new ApiException("Listing/seller not found on negotiation");
        }

        Integer expectedSellerId = n.getListing().getSeller().getId();
        if (!body.getSenderId().equals(expectedSellerId)) {
            throw new ApiException("senderId must match the negotiation seller id");
        }

        NegotiationMessage sellerMsg = new NegotiationMessage();
        sellerMsg.setNegotiation(n);
        sellerMsg.setSenderType("seller");
        sellerMsg.setSenderId(body.getSenderId());
        sellerMsg.setContent(body.getContent());
        sellerMsg.setCreatedAt(LocalDateTime.now());
        messageRepository.save(sellerMsg);

        String mode = n.getMode();
        if (mode != null) {
            if (mode.equals("ai-assisted")) {

                List<NegotiationMessage> history =
                        messageRepository.findAllByNegotiation_IdOrderByCreatedAtAsc(negotiationId);

                String userMessage = buildUserMessageForAi(n, history);

                String aiText = aiService.chat("bargain_negotiation", userMessage);
                if (aiText == null) throw new ApiException("AI returned no content");
                if (aiText.trim().isEmpty()) throw new ApiException("AI returned empty content");

                NegotiationMessage aiMsg = new NegotiationMessage();
                aiMsg.setNegotiation(n);
                aiMsg.setSenderType("ai");
                aiMsg.setSenderId(null);
                aiMsg.setContent(aiText.trim());
                aiMsg.setCreatedAt(LocalDateTime.now());
                messageRepository.save(aiMsg);
            }
        }
    }

    private String buildUserMessageForAi(Negotiation negotiation, List<NegotiationMessage> history) {
        StringBuilder sb = new StringBuilder();

        sb.append("بيانات الإعلان:\n");
        appendListingBlock(sb, negotiation.getListing());

        sb.append("\nسجل المحادثة كاملًا (من الأقدم إلى الأحدث):\n");
        sb.append(renderHistory(history));

        return sb.toString();
    }

    private void appendListingBlock(StringBuilder sb, Listing listing) {
        if (listing == null) {
            sb.append("(لا توجد بيانات إعلان)\n");
            return;
        }

        sb.append("- العنوان: ").append(show(listing.getTitle())).append("\n");
        sb.append("- النوع: ").append(show(listing.getType())).append("\n");
        sb.append("- الحالة (الإعلان): ").append(show(listing.getStatus())).append("\n");
        sb.append("- المدينة: ").append(show(listing.getCity())).append("\n");
        sb.append("- أقل سعر معلَن (SAR): ").append(show(listing.getLeast_price())).append("\n");
        sb.append("- الوصف: ").append(show(listing.getDescription())).append("\n");

        String t = listing.getType();
        if (t == null) t = "";
        t = t.toLowerCase();

        if (t.equals("car")) {
            Object car = listing.getCarListing();
            sb.append("\n[تفاصيل السيارة]\n");
            if (car == null) {
                sb.append("  (تفاصيل السيارة غير متوفرة)\n");
            } else {
                sb.append("  الماركة/الموديل: ").append(show(tryGetStr(car, "getMake"))).append(" / ")
                        .append(show(tryGetStr(car, "getModel"))).append("\n");
                sb.append("  السنة: ").append(show(tryGetStr(car, "getYear"))).append("\n");
                sb.append("  العداد: ").append(show(tryGetStr(car, "getMileage"))).append("\n");
                sb.append("  اللون: ").append(show(tryGetStr(car, "getColor"))).append("\n");
                sb.append("  الوقود: ").append(show(tryGetStr(car, "getFuelType", "getFuel_type"))).append("\n");
                sb.append("  السعر المطلوب (SAR): ").append(show(tryGetStr(car, "getPrice", "getLeast_price"))).append("\n");
                sb.append("  وصف إضافي: ").append(show(tryGetStr(car, "getDescription"))).append("\n");
            }
        } else if (t.equals("real_estate") || t.equals("real-estate") || t.equals("realestate")) {
            Object re = listing.getRealEstateListing();
            sb.append("\n[تفاصيل العقار]\n");
            if (re == null) {
                sb.append("  (تفاصيل العقار غير متوفرة)\n");
            } else {
                sb.append("  الموقع: ").append(show(tryGetStr(re, "getLocation", "getCity"))).append("\n");
                sb.append("  المساحة (م²): ").append(show(tryGetStr(re, "getArea", "getSquareMeter"))).append("\n");
                sb.append("  السعر المطلوب (SAR): ").append(show(tryGetStr(re, "getPrice", "getLeast_price"))).append("\n");
                sb.append("  النوع: ").append(show(tryGetStr(re, "getType"))).append("\n");
                sb.append("  إيجار؟ ").append(show(tryGetStr(re, "getIsRental"))).append("\n");
                sb.append("  الغرف/الدورات: ").append(show(tryGetStr(re, "getRooms"))).append(" / ")
                        .append(show(tryGetStr(re, "getBathrooms"))).append("\n");
                sb.append("  الحي: ").append(show(tryGetStr(re, "getNeighborhood"))).append("\n");
                sb.append("  وصف إضافي: ").append(show(tryGetStr(re, "getDescription"))).append("\n");
            }
        }
    }

    private static String show(Object v) {
        if (v == null) return "-";
        return String.valueOf(v);
    }

    private String renderHistory(List<NegotiationMessage> history) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < history.size()) {
            NegotiationMessage m = history.get(i);
            String role = m.getSenderType();
            if (role == null) role = "unknown";
            String tag;
            if (role.equals("buyer")) tag = "[Buyer]";
            else if (role.equals("seller")) tag = "[Seller]";
            else if (role.equals("ai")) tag = "[AI]";
            else tag = "[Unknown]";

            String ts = "";
            if (m.getCreatedAt() != null) {
                ts = " (" + m.getCreatedAt().toString() + ")";
            }

            sb.append(tag).append(ts).append(" ").append(m.getContent()).append("\n");
            i = i + 1;
        }
        return sb.toString();
    }

    private String tryGetStr(Object obj, String... getters) {
        if (obj == null) return null;
        if (getters == null) return null;

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

    public String summarizeAndSet(Integer negotiationId) {
        if (negotiationId == null) throw new ApiException("negotiationId is required");

        Negotiation negotiation = negotiationRepository.findNegotiationById(negotiationId);
        if (negotiation == null) throw new ApiException("Negotiation not found: " + negotiationId);

        List<NegotiationMessage> history =
                messageRepository.findAllByNegotiation_IdOrderByCreatedAtAsc(negotiationId);

        if (history == null || history.isEmpty()) {
            negotiation.setSummary("لا توجد رسائل في هذه المفاوضة بعد.");
            negotiationRepository.save(negotiation);
            return negotiation.getSummary();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("سجل المحادثة كاملًا (من الأقدم إلى الأحدث):\n");
        sb.append(renderHistory(history));

        String summary = aiService.chat("negotiation_summary", sb.toString());
        if (summary == null || summary.trim().isEmpty()) {
            throw new ApiException("AI returned empty summary");
        }

        negotiation.setSummary(summary.trim());
        negotiationRepository.save(negotiation);
        return negotiation.getSummary();
    }



    public List<ChatLineDTOOut> listAscSimple(Integer negotiationId) {
        if (negotiationId == null) throw new ApiException("negotiationId is required");

        Negotiation n = negotiationRepository.findNegotiationById(negotiationId);
        if (n == null) throw new ApiException("Negotiation not found: " + negotiationId);

        List<NegotiationMessage> msgs =
                messageRepository.findAllByNegotiation_IdOrderByCreatedAtAsc(negotiationId);

        ArrayList<ChatLineDTOOut> out = new ArrayList<>();
        int i = 0;
        while (i < msgs.size()) {
            NegotiationMessage m = msgs.get(i);
            ChatLineDTOOut line = new ChatLineDTOOut(
                    m.getSenderType(),
                    m.getSenderId(),
                    m.getContent(),
                    m.getCreatedAt()
            );
            out.add(line);
            i = i + 1;
        }
        return out;
    }


}
