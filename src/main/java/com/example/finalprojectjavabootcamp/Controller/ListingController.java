package com.example.finalprojectjavabootcamp.Controller;

import com.example.finalprojectjavabootcamp.Api.ApiResponse;
import com.example.finalprojectjavabootcamp.Service.ListingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/listing")
public class ListingController {

    private final ListingService listingService;

    @GetMapping("/get/all")
    public ResponseEntity<?> getAllListings() {
        return ResponseEntity.status(HttpStatus.OK).body(listingService.getAllListings());
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<?> getListingsBySeller(@PathVariable Integer sellerId) {
        return ResponseEntity.status(HttpStatus.OK).body(listingService.getListingBySellerId(sellerId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getListingsByStatus(@PathVariable String status) {
        return ResponseEntity.status(HttpStatus.OK).body(listingService.getListingByStatus(status));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<?> getListingsByType(@PathVariable String type) {
        return ResponseEntity.status(HttpStatus.OK).body(listingService.getListingByType(type));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchListings(@RequestParam String query) {
        return ResponseEntity.status(HttpStatus.OK).body(listingService.searchListing(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getListingById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(listingService.getListingById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteListing(@PathVariable Integer id) {
        listingService.deleteListing(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Listing deleted successfully"));
    }
}