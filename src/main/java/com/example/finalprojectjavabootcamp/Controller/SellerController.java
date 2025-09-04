package com.example.finalprojectjavabootcamp.Controller;

import com.example.finalprojectjavabootcamp.DTOIN.SellerDTOIn;
import com.example.finalprojectjavabootcamp.Service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    @PostMapping("/register")
    public ResponseEntity<?> registerSeller(@RequestBody SellerDTOIn dto) {
        sellerService.registerSeller(dto);
        return ResponseEntity.status(200).body("Seller registered successfully");
    }

    @PutMapping("/update/{sellerId}")
    public ResponseEntity<?> updateSeller(@PathVariable Integer sellerId,
                                          @RequestBody SellerDTOIn dto) {
        sellerService.updateSeller(sellerId, dto);
        return ResponseEntity.status(200).body("Seller updated successfully");
    }



}
