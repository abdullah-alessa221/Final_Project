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
        if (negotiationId == null) throw new ApiException("negotiationId is required");
        if (body == null) throw new ApiException("Request body is required");
        if (body.getSenderId() == null) throw new ApiException("senderId is required");

        String content = body.getContent();
        if (content == null) throw new ApiException("content is required");
        content = content.trim();
        if (content.isEmpty()) throw new ApiException("content is required");
        if (content.length() > 4000) throw new ApiException("content must be at most 4000 characters");

        Negotiation n = negotiationRepository.findNegotiationById(negotiationId);
        if (n == null) throw new ApiException("Negotiation not found: " + negotiationId);

        if (n.getBuyer() == null) throw new ApiException("Negotiation has no buyer");
        if (!body.getSenderId().equals(n.getBuyer().getId())) {
            throw new ApiException("senderId must match the negotiation buyer id");
        }

//        String st = n.getStatus();
//        if (st != null) {
//            String s = st.toLowerCase();
//            if (s.equals("accepted") || s.equals("rejected")) {
//                throw new ApiException("Negotiation is closed; cannot send messages");
//            }
//        }

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


    public NegotiationMessage sendBySeller(Integer negotiationId, NegotiationMessageDTOIn body) {
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
        sellerMsg = messageRepository.save(sellerMsg);

        String mode = n.getMode();
        if (mode != null && mode.equals("ai-assisted")) {
            List<NegotiationMessage> history =
                    messageRepository.findAllByNegotiation_IdOrderByCreatedAtAsc(negotiationId);

            String dynamicMessage = buildNegotiationContext(n, history);
            String aiText = aiService.chat("bargain_negotiation", dynamicMessage);
            if (aiText == null || aiText.trim().isEmpty()) {
                throw new ApiException("AI returned no content");
            }

            NegotiationMessage aiMsg = new NegotiationMessage();
            aiMsg.setNegotiation(n);
            aiMsg.setSenderType("ai");
            aiMsg.setSenderId(null);
            aiMsg.setContent(aiText.trim());
            aiMsg.setCreatedAt(LocalDateTime.now());
            return messageRepository.save(aiMsg);
        }

        return sellerMsg;
    }

    private String buildNegotiationContext(Negotiation negotiation, List<NegotiationMessage> history) {
        StringBuilder sb = new StringBuilder();

        sb.append("أنت وكيل تفاوض (ذكاء اصطناعي) تمثّل المشتري فقط. ")
                .append("اجعل ردودك قصيرة ومهذّبة. يمكنك اقتراح عرض باسم المشتري عند الحاجة، ")
                .append("ولا تكشف أي معلومات خاصة مثل السعر المستهدف إن وُجد.\n\n");

        sb.append("سياق التفاوض:\n");
        sb.append("- رقم التفاوض: ").append(negotiation.getId()).append("\n");

        Listing listing = negotiation.getListing();
        if (listing != null) {
            sb.append("- رقم الإعلان: ").append(listing.getId()).append("\n");
            if (listing.getSeller() != null) {
                sb.append("- رقم البائع: ").append(listing.getSeller().getId()).append("\n");
            }
        }
        if (negotiation.getSummary() != null) {
            sb.append("- ملاحظات/متطلبات المشتري: ").append(negotiation.getSummary()).append("\n");
        }
        if (negotiation.getAgreedPrice() != null) {
            sb.append("- السعر المتفق عليه حاليًا: ").append(negotiation.getAgreedPrice()).append(" SAR\n");
        }

        sb.append("\nسجل المحادثة كاملًا (من الأقدم إلى الأحدث):\n");
        sb.append(renderHistory(history));

        sb.append("\nأرسل ردّك التالي بصفتك المشتري:\n");
        return sb.toString();
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

    private String renderHistory(List<NegotiationMessage> history) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < history.size()) {
            NegotiationMessage m = history.get(i);
            String role = m.getSenderType();
            if (role == null) role = "unknown";
            if ("buyer".equals(role)) sb.append("[Buyer] ");
            else if ("seller".equals(role)) sb.append("[Seller] ");
            else if ("ai".equals(role)) sb.append("[AI] ");
            else sb.append("[Unknown] ");
            sb.append(m.getContent()).append("\n");
            i = i + 1;
        }
        return sb.toString();
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
