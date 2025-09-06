package com.example.finalprojectjavabootcamp.Controller;

import com.example.finalprojectjavabootcamp.Api.ApiResponse;
import com.example.finalprojectjavabootcamp.Service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscription")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;


    @PostMapping("/cancel/{subscriptionId}")
    public ResponseEntity<?> cancelSubscription(@PathVariable Integer subscriptionId) {
        subscriptionService.cancelSubscription(subscriptionId);
        return ResponseEntity.ok(new ApiResponse("Subscription cancelled successfully."));
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(subscriptionService.getAll());
    }
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> getSubscriptionById(@PathVariable Integer id){
        return ResponseEntity.ok(subscriptionService.getSubscriptionById(id));
    }


    //EXTRA:
    @PostMapping("/monthly/{sellerId}")
    public ResponseEntity<?> subscribeMonthly(@PathVariable Integer sellerId){
        subscriptionService.subscribeMonthly(sellerId);
        return ResponseEntity.status(200).body(new ApiResponse("Monthly subscription activated successfully."));
    }

    @PostMapping("/yearly/{sellerId}")
    public ResponseEntity<?> subscribeYearly(@PathVariable Integer sellerId){
        subscriptionService.subscribeYearly(sellerId);
        return ResponseEntity.ok(new ApiResponse("Yearly subscription activated successfully."));
    }

    @PutMapping("/pause/{subscriptionId}")
    public ResponseEntity<String> pauseSubscription(@PathVariable Integer subscriptionId) {
        subscriptionService.pauseSubscription(subscriptionId);
        return ResponseEntity.ok("Subscription paused successfully.");
    }

    @PutMapping("/resume/{subscriptionId}")
    public ResponseEntity<String> resumeSubscription(@PathVariable Integer subscriptionId) {
        subscriptionService.resumeSubscription(subscriptionId);
        return ResponseEntity.ok("Subscription resumed successfully.");
    }



}
