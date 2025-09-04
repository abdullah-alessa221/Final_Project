package com.example.finalprojectjavabootcamp.Controller;

import com.example.finalprojectjavabootcamp.Api.ApiResponse;
import com.example.finalprojectjavabootcamp.DTOIN.BuyerDTOIn;
import com.example.finalprojectjavabootcamp.Model.Buyer;
import com.example.finalprojectjavabootcamp.Service.BuyerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/buyer")
@RequiredArgsConstructor
public class BuyerController {

    private final BuyerService buyerService;

    @PostMapping("/register")
    public ResponseEntity<?> registerBuyer(@RequestBody BuyerDTOIn dto){
        buyerService.registerBuyer(dto);
        return ResponseEntity.status(200).body(new ApiResponse("Buyer registered successfully"));
    }

    @PutMapping("/update/{buyerId}")
    public ResponseEntity<?> updateBuyer(@PathVariable Integer buyerId, @RequestBody BuyerDTOIn dto) {
        buyerService.updateBuyer(buyerId, dto);
        return ResponseEntity.ok(new ApiResponse("Buyer updated successfully."));
    }
}
