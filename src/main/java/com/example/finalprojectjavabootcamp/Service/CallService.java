package com.example.finalprojectjavabootcamp.Service;

import com.example.finalprojectjavabootcamp.Repository.CallRepository;
import com.example.finalprojectjavabootcamp.Repository.SellerRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.finalprojectjavabootcamp.Api.ApiException;
import com.example.finalprojectjavabootcamp.Model.Call;
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
public class CallService {
    @Value("${VAPI_KEY}")
    private String vapiKey;

    private final CallRepository callRepository;
    private final SellerRepository sellerRepository;

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();


    public List<Call> retrieveAndUpdateAllCalls(Integer sellerId) throws ApiException {
        Seller seller = sellerRepository.findSellerById(sellerId);
        if (seller == null) {
            throw new ApiException("Seller not found");
        }
        if (seller.getSubscription() == null || seller.getSubscription().getPhoneNumberId() == null || seller.getSubscription().getEndDate().isBefore(LocalDateTime.now())) {
            throw new ApiException("Seller has no subscription");
        }

        try {
            List<Call> fetchedCalls = fetchCallsFromApi(seller.getSubscription().getPhoneNumberId());

            List<Call> savedCalls = new ArrayList<>();
            for (Call call : fetchedCalls) {
                call.setSeller(seller);
                Optional<Call> existingCall = callRepository.findCallById(call.getId());
                if (existingCall.isEmpty()) {
                    savedCalls.add(callRepository.save(call));
                }
            }

            return savedCalls;

        } catch (Exception e) {
            throw new ApiException("Error retrieving and updating calls: " + e.getMessage());
        }
    }

    public List<Call> getAllCalls() {
        return callRepository.findAll();
    }

    public Optional<Call> getCallById(String id) {
        return callRepository.findCallById(id);
    }

    public List<Call> getCallsByPhoneNumber(String phoneNumber) {
        return callRepository.findByCustomerNumber(phoneNumber);
    }

    public List<Call> getCallsByStartedAt(LocalDateTime startedAt) {
        return callRepository.findByStartedAt(startedAt);
    }

    public List<Call> getCallsByStatus(String status) {
        return callRepository.findByStatus(status);
    }

    public List<Call> getCallsBySeller(Integer sellerId) {
        Seller seller = sellerRepository.findSellerById(sellerId);
        if (seller == null) {
            throw new ApiException("Seller not found");
        }
        return callRepository.findBySeller(seller);
    }

    public List<Call> getCallsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return callRepository.findByStartedAtBetween(startDate, endDate);
    }

    public List<Call> getCallsBySellerAndStatus(Integer sellerId, String status) {
        Seller seller = sellerRepository.findSellerById(sellerId);
        if (seller == null) {
            throw new ApiException("Seller not found");
        }
        return callRepository.findBySellerAndStatus(seller, status);
    }

    private List<Call> fetchCallsFromApi(String phoneNumberId) throws ApiException {
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

            List<Call> callList = new ArrayList<>();

            if (rootNode.isArray()) {
                for (JsonNode callNode : rootNode) {
                    Call call = mapJsonToCall(callNode);
                    if (call != null) {
                        callList.add(call);
                    }
                }
            } else {
                Call call = mapJsonToCall(rootNode);
                if (call != null) {
                    callList.add(call);
                }
            }

            return callList;

        } catch (Exception e) {
            throw new ApiException("Unexpected error while fetching calls from API: " + e.getMessage());
        }
    }

    private Call mapJsonToCall(JsonNode callNode) {
        try {
            Call call = new Call();

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
        // Try different possible locations for transcript summary
        if (callNode.has("analysis") && callNode.get("analysis").has("summary")) {
            return callNode.get("analysis").get("summary").asText();
        }
        if (callNode.has("transcript") && callNode.get("transcript").has("summary")) {
            return callNode.get("transcript").get("summary").asText();
        }
        if (callNode.has("summary")) {
            return callNode.get("summary").asText();
        }
        return null; // No summary found
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