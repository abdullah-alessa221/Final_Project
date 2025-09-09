package com.example.finalprojectjavabootcamp.Controller;

import com.example.finalprojectjavabootcamp.Api.ApiResponse;
import com.example.finalprojectjavabootcamp.Model.User;
import com.example.finalprojectjavabootcamp.Service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PutMapping("/set-phone-id/{subscriptionId}/{phoneId}")
    public ResponseEntity<?> setPhoneId(@PathVariable Integer subscriptionId, @PathVariable String phoneId){
        subscriptionService.setPhoneNumber(subscriptionId, phoneId);
        return ResponseEntity.ok(new ApiResponse("Phone number set successfully."));
    }

    //EXTRA:
    @PostMapping("/monthly")
    public ResponseEntity<?> subscribeMonthly(@AuthenticationPrincipal User user){
        subscriptionService.subscribeMonthly(user.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Monthly subscription is pending, pay to activate."));
    }

    @PostMapping("/yearly")
    public ResponseEntity<?> subscribeYearly(@AuthenticationPrincipal User user){
        subscriptionService.subscribeYearly(user.getId());
        return ResponseEntity.ok(new ApiResponse("Yearly subscription is pending, pay to activate."));
    }

}
