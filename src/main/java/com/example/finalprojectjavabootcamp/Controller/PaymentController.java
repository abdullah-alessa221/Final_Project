package com.example.finalprojectjavabootcamp.Controller;

import com.example.finalprojectjavabootcamp.Api.ApiResponse;
import com.example.finalprojectjavabootcamp.DTOIN.PaymentDTO;
import com.example.finalprojectjavabootcamp.Service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllPayments() {
        return ResponseEntity.status(200).body(paymentService.getAllPayments());
    }

    @GetMapping("/get-payment-by-id/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable Integer id) {
            return ResponseEntity.status(200).body(paymentService.getPaymentById(id));

    }

    @GetMapping("/get-payment-status/{paymentId}")
    public ResponseEntity<?> getPaymentStatus(@PathVariable String paymentId) {
            return ResponseEntity.status(200).body(paymentService.getPaymentStatus(paymentId));
    }

    @PostMapping("/process-payment/{negotiationId}")
    public ResponseEntity<?> processPayment(@PathVariable Integer negotiationId, @RequestBody PaymentDTO paymentDTO) {
            return paymentService.processPayment(negotiationId, paymentDTO);
    }

    @PostMapping("/process-subscription-payment/{subscriptionId}")
    public ResponseEntity<?> processPaymentForSubscription( @PathVariable Integer subscriptionId, @RequestBody PaymentDTO paymentDTO) {
            return paymentService.processPaymentForSubscription( subscriptionId, paymentDTO);

    }

//    @PostMapping("/callback")
//    public ResponseEntity<?> handlePaymentCallback(
//            @RequestParam String id,
//            @RequestParam String status,
//            @RequestParam String amount,
//            @RequestParam String message) {
//        try {
//            paymentService.handlePaymentCallback(id, status, amount, message);
//            return ResponseEntity.status(200).body(new ApiResponse("Payment callback processed successfully"));
//        } catch (Exception e) {
//            return ResponseEntity.status(400).body(new ApiResponse("Callback processing failed: " + e.getMessage()));
//       }
//    }

@GetMapping("/callback")
public ResponseEntity<?> handlePaymentCallback(@RequestParam String id,
                                               @RequestParam String status,
                                               @RequestParam(required = false) String amount,
                                               @RequestParam(required = false) String message) {
    paymentService.handlePaymentCallback(id, status);
    return ResponseEntity.status(200).body(new ApiResponse("Payment status("+status+")"));
}

}