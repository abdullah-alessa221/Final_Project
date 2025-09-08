package com.example.finalprojectjavabootcamp.Service;

import com.example.finalprojectjavabootcamp.DTOIN.InvoiceDTO;
import com.example.finalprojectjavabootcamp.Model.Negotiation;
import com.example.finalprojectjavabootcamp.Model.Payment;
import com.example.finalprojectjavabootcamp.Model.Subscription;
import com.example.finalprojectjavabootcamp.Repository.PaymentRepository;
import com.example.finalprojectjavabootcamp.Repository.SubscriptionRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import com.example.finalprojectjavabootcamp.Api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final NegotiationRepository negotiationRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Value("${moyasar.api.key}")
    private String apiKey;

    private static final String MOYASAR_API_URL = "https://api.moyasar.com/v1/payments";


    public List<Payment> getAllPayments(){
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(Integer id) {
        Payment invoice = paymentRepository.findPaymentById(id);
        if (invoice == null) {
            throw new ApiException("Payment not found");
        }
        return invoice;
    }




    public String getPaymentStatus(String paymentId){
        HttpHeaders headers = new HttpHeaders();

        //prepare headers
        headers.setBasicAuth(apiKey,"");
        headers.setContentType(MediaType.APPLICATION_JSON);

        //create the request entity
        HttpEntity<String> entity = new HttpEntity<>(headers);

        //call the api

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
                MOYASAR_API_URL + "/" + paymentId ,HttpMethod.GET, entity, String.class
        );


        //return the response
        return response.getBody();
    }




    public ResponseEntity<String> processPayment(Integer negotiationId , InvoiceDTO invoiceDTO){

        Negotiation negotiation = negotiationRepository.findNegotiationById(negotiationId);

        if (negotiation ==  null){
            throw new ApiException("negotiation is not found");
        }

        if (!negotiation.getStatus().equalsIgnoreCase("accepted")){
            throw new ApiException("cannot continue payment, negotiation status: "+negotiation.getStatus());
        }

        String callbackUrl = "http://localhost:8080/api/v1/invoice/callback";


        //create the body
        String requestBody =String.format("source[type]=card&source[name]=%s&source[number]=%s&source[cvc]=%s&source[month]=%s&source[year]=%s&amount=%d&currency=%s&callback_url=%s",
                invoiceDTO.getCardName(),
                invoiceDTO.getCardNumber(),
                invoiceDTO.getCardCvc(),
                invoiceDTO.getCardMonth(),
                invoiceDTO.getCardYear(),
                (int) (negotiation.getAgreedPrice() * 1),
                "SAR",
                callbackUrl);


        //set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(apiKey,"");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        //send the request

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<JsonNode> response = restTemplate.exchange(
                MOYASAR_API_URL,
                HttpMethod.POST,
                entity,
                JsonNode.class);

        // Parse JSON response
        JsonNode root = response.getBody();
        String status = root.path("status").asText();          // e.g. "paid", "failed", "initiated"
        String paymentId = root.path("id").asText(null);


        Payment payment = new Payment(null,negotiation,null,null,false,negotiation.getAgreedPrice(),"pending",paymentId,null);
        paymentRepository.save(payment);

        return ResponseEntity.status(response.getStatusCode()).body(response.getBody().toString());
    }



    public void handlePaymentCallback(String id, String status, String amount, String message) {
        Payment payment = paymentRepository.findPaymentByPaymentId(id);
        if (payment == null) {
            throw new ApiException("Payment not found");
        }

        if (payment.getIsSubscription()) {
            if (status.equalsIgnoreCase("paid")) {
                payment.setStatus("paid");
                paymentRepository.save(payment);
            }
        } else {
            if (status.equalsIgnoreCase("paid")) {
                Subscription subscription = payment.getSubscription();
                subscription.setStatus("ACTIVE");
                subscription.setStartDate(LocalDateTime.now());
                subscription.setEndDate(LocalDateTime.now().plusMonths(1));
                subscriptionRepository.save(subscription);
                payment.setStatus("paid");
                paymentRepository.save(payment);
            }
        }
    }

    public ResponseEntity<String> processPaymentForSubscription(Integer SellerId, Integer subscriptionId, InvoiceDTO invoiceDTO) {
        Subscription subscription = subscriptionRepository.findSubscriptionById(subscriptionId);
        if (subscription == null){
            throw new ApiException("subscription not found");
        }

        double amount = 59;
        String callbackUrl = "http://localhost:8080/api/v1/invoice/callback";

        //create the body
        String requestBody =String.format("source[type]=card&source[name]=%s&source[number]=%s&source[cvc]=%s&source[month]=%s&source[year]=%s&amount=%d&currency=%s&callback_url=%s",
                invoiceDTO.getCardName(),
                invoiceDTO.getCardNumber(),
                invoiceDTO.getCardCvc(),
                invoiceDTO.getCardMonth(),
                invoiceDTO.getCardYear(),
                (int)(amount * 100),
                "SAR",
                callbackUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(apiKey, "");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        ResponseEntity<JsonNode> resp = new RestTemplate().exchange(
                MOYASAR_API_URL, HttpMethod.POST, new HttpEntity<>(requestBody, headers), JsonNode.class);


        JsonNode root = resp.getBody();

        if (root == null) throw new ApiException("Empty gateway response");

        String status = root.path("status").asText();
        String paymentId = root.path("id").asText(null);


        Payment payment = new Payment(null,null,null,subscription,true,amount,"pending",paymentId,null);
        paymentRepository.save(payment);

        return ResponseEntity.status(resp.getStatusCode()).body(root.toString());
    }

}