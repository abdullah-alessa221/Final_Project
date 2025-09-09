package com.example.finalprojectjavabootcamp.Service;

import com.example.finalprojectjavabootcamp.Repository.CallLogRepository;
import com.example.finalprojectjavabootcamp.Repository.SellerRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.finalprojectjavabootcamp.Api.ApiException;
import com.example.finalprojectjavabootcamp.Model.CallLog;
import com.example.finalprojectjavabootcamp.Model.Seller;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class CallLogService {
    @Value("${VAPI_KEY}")
    private String vapiKey;

    private final CallLogRepository callRepository;
    private final SellerRepository sellerRepository;
    private final AiService aiService;

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();


    public List<CallLog> retrieveAndUpdateAllCallLogs(Integer sellerId) throws ApiException {
        Seller seller = sellerRepository.findSellerById(sellerId);
        if (seller == null) {
            throw new ApiException("Seller not found");
        }
        if (seller.getSubscription() == null || seller.getSubscription().getPhoneNumberId() == null || seller.getSubscription().getEndDate().isBefore(LocalDateTime.now())) {
            throw new ApiException("Seller has no subscription");
        }

        try {
            List<CallLog> fetchedCallLogs = fetchCallLogsFromApi(seller.getSubscription().getPhoneNumberId());

            List<CallLog> savedCallLogs = new ArrayList<>();
            for (CallLog call : fetchedCallLogs) {
                call.setSeller(seller);
                Optional<CallLog> existingCallLog = callRepository.findCallLogById(call.getId());
                if (existingCallLog.isEmpty()) {
                    savedCallLogs.add(callRepository.save(call));
                }
            }

            return savedCallLogs;

        } catch (Exception e) {
            throw new ApiException("Error retrieving and updating calls: " + e.getMessage());
        }
    }

    public List<CallLog> getAllCallLogs() {
        return callRepository.findAll();
    }

    public Optional<CallLog> getCallLogById(String id) {
        return callRepository.findCallLogById(id);
    }

    public List<CallLog> getCallLogsByPhoneNumber(String phoneNumber) {
        return callRepository.findByCustomerNumber(phoneNumber);
    }

    public List<CallLog> getCallLogsByStartedAt(LocalDateTime startedAt) {
        return callRepository.findByStartedAt(startedAt);
    }

    public List<CallLog> getCallLogsByStatus(String status) {
        return callRepository.findByStatus(status);
    }

    public List<CallLog> getCallLogsBySeller(Integer sellerId) {
        Seller seller = sellerRepository.findSellerById(sellerId);
        if (seller == null) {
            throw new ApiException("Seller not found");
        }
        return callRepository.findBySeller(seller);
    }

    public List<CallLog> getCallLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return callRepository.findByStartedAtBetween(startDate, endDate);
    }

    public List<CallLog> getCallLogsBySellerAndStatus(Integer sellerId, String status) {
        Seller seller = sellerRepository.findSellerById(sellerId);
        if (seller == null) {
            throw new ApiException("Seller not found");
        }
        return callRepository.findBySellerAndStatus(seller, status);
    }

    private List<CallLog> fetchCallLogsFromApi(String phoneNumberId) throws ApiException {
        try {
            HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.vapi.ai/call").newBuilder();
            if (phoneNumberId != null && !phoneNumberId.isEmpty()) {
                urlBuilder.addQueryParameter("phoneNumberId", phoneNumberId);
            }

            Request request = new Request.Builder()
                    .url(urlBuilder.build())
                    .addHeader("Authorization", "Bearer " + vapiKey)
                    .addHeader("Accept", "application/json")
                    .get()
                    .build();

            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                String responseBody = response.body() != null ? response.body().string() : "No response body";
                throw new ApiException("Failed to retrieve calls from API: " + response.code() + " - " + responseBody);
            }

            String responseBody = response.body().string();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            List<CallLog> callList = new ArrayList<>();

            if (rootNode.isArray()) {
                for (JsonNode callNode : rootNode) {
                    CallLog call = mapJsonToCallLog(callNode);
                    if (call != null) {
                        callList.add(call);
                    }
                }
            } else {
                CallLog call = mapJsonToCallLog(rootNode);
                if (call != null) {
                    callList.add(call);
                }
            }

            return callList;

        } catch (Exception e) {
            throw new ApiException("Unexpected error while fetching calls from API: " + e.getMessage());
        }
    }

    private CallLog mapJsonToCallLog(JsonNode callNode) {
        try {
            CallLog call = new CallLog();

            // Extract id
            if (callNode.has("id")) {
                call.setId(callNode.get("id").asText());
            } else {
                return null; // Skip if no ID
            }

            // Extract transcript summary
            String summary = extractTranscriptSummary(callNode);
            call.setTranscriptSummary(summary);

            // Extract customer number
            if (callNode.has("customer") && callNode.get("customer").has("number")) {
                call.setCustomerNumber(callNode.get("customer").get("number").asText());
            }

            // Extract cost
            if (callNode.has("cost")) {
                call.setCost(callNode.get("cost").asDouble());
            } else {
                call.setCost(0.0); // Default value
            }

            // Extract and parse startedAt
            if (callNode.has("startedAt")) {
                LocalDateTime startedAt = parseDateTime(callNode.get("startedAt").asText());
                call.setStartedAt(startedAt);
            }

            // Extract and parse endedAt
            if (callNode.has("endedAt")) {
                LocalDateTime endedAt = parseDateTime(callNode.get("endedAt").asText());
                call.setEndedAt(endedAt);
            }

            // Extract status
            if (callNode.has("status")) {
                call.setStatus(callNode.get("status").asText());
            } else {
                call.setStatus("unknown"); // Default value
            }

            return call;

        } catch (Exception e) {
            return null;
        }
    }

    private String extractTranscriptSummary(JsonNode callNode) {
        // 1) Extract
        String summary = null;

        JsonNode n = callNode.path("analysis").path("summary");
        if (n.isTextual()) summary = n.asText();

        if (summary == null) {
            n = callNode.path("transcript").path("summary");
            if (n.isTextual()) summary = n.asText();
        }

        if (summary == null) {
            n = callNode.path("summary");
            if (n.isTextual()) summary = n.asText();
        }

        if (summary == null || summary.isBlank()) return null;

        // 2) Always translate to Arabic
        try {
            return aiService.chat("translate_to_arabic", summary);
        } catch (Exception e) {
            // If you truly want Arabic-only, return null here instead of the English summary.
            // return null;
            return summary; // graceful fallback (optional)
        }
    }


    private LocalDateTime parseDateTime(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.isEmpty()) {
            return null;
        }

        try {
            // Try ISO format first
            return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME);
        } catch (DateTimeParseException e1) {
            try {
                // Try with offset
                return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            } catch (DateTimeParseException e2) {
                try {
                    // Try custom format if needed
                    return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
                } catch (DateTimeParseException e3) {
                    return LocalDateTime.now(); // Default to current time
                }
            }
        }
    }

}