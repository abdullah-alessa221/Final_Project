package com.example.finalprojectjavabootcamp.Controller;

import com.example.finalprojectjavabootcamp.Api.ApiResponse;
import com.example.finalprojectjavabootcamp.DTOIN.RatingDTOIn;
import com.example.finalprojectjavabootcamp.Service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/rating")
public class Ratingcontroller {

    private final RatingService ratingService;

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<?> getAllByBuyer(@PathVariable Integer buyerId) {
        return ResponseEntity.ok(ratingService.getAllRatingsByBuyerId(buyerId));
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<?> getBySeller(@PathVariable Integer sellerId) {
        return ResponseEntity.ok(ratingService.getRatingsBySellerId(sellerId));
    }

    @PostMapping("/buyer/{buyerId}/seller/{sellerId}/{paymentId}")
    public ResponseEntity<?> addRating(@PathVariable Integer buyerId,
                                       @PathVariable Integer sellerId,
                                       @PathVariable Integer paymentId,
                                       @Valid @RequestBody RatingDTOIn body) {
        ratingService.addRating(buyerId, sellerId, paymentId, body);
        return ResponseEntity.ok(new ApiResponse("rating added successfully"));
    }
}
