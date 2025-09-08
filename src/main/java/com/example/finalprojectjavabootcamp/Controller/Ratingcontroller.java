package com.example.finalprojectjavabootcamp.Controller;

import com.example.finalprojectjavabootcamp.Api.ApiResponse;
import com.example.finalprojectjavabootcamp.DTOIN.RatingDTOIn;
import com.example.finalprojectjavabootcamp.Model.User;
import com.example.finalprojectjavabootcamp.Service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.buf.UEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/rating")
public class Ratingcontroller {

    private final RatingService ratingService;

    @GetMapping("/buyer")
    public ResponseEntity<?> getAllByBuyer(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ratingService.getAllRatingsByBuyerId(user.getId()));
    }

    @GetMapping("/seller")
    public ResponseEntity<?> getBySeller(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ratingService.getRatingsBySellerId(user.getId()));
    }

    @PostMapping("/buyer/seller/{sellerId}/{paymentId}")
    public ResponseEntity<?> addRating(@AuthenticationPrincipal User user,
                                       @PathVariable Integer sellerId,
                                       @PathVariable Integer paymentId,
                                       @Valid @RequestBody RatingDTOIn body) {
        ratingService.addRating(user.getId(), sellerId, paymentId, body);
        return ResponseEntity.ok(new ApiResponse("rating added successfully"));
    }
}
