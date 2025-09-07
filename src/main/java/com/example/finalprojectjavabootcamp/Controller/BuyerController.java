package com.example.finalprojectjavabootcamp.Controller;

import com.example.finalprojectjavabootcamp.Api.ApiResponse;
import com.example.finalprojectjavabootcamp.DTOIN.BuyerDTOIn;
import com.example.finalprojectjavabootcamp.Model.Buyer;
import com.example.finalprojectjavabootcamp.Service.BuyerService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/buyer")
@RequiredArgsConstructor
public class BuyerController {

    private final BuyerService buyerService;


    @PutMapping("/update/{buyerId}")
    public ResponseEntity<?> updateBuyer(@PathVariable Integer buyerId,@Valid @RequestBody BuyerDTOIn dto) {
        buyerService.updateBuyer(buyerId, dto);
        return ResponseEntity.ok(new ApiResponse("Buyer updated successfully."));
    }

    //EXTRA:


    @PostMapping("/register/request-otp")
    public ResponseEntity<?> requestOtp(@RequestBody BuyerDTOIn dto){
        buyerService.requestOtp(dto);
        return ResponseEntity.ok(new ApiResponse("الى رقمك OTP تم ارسال "));
    }


    @PostMapping("/register/confirm-otp")
    public ResponseEntity<?> confirmOtp(
            @RequestBody BuyerDTOIn dto,
            @RequestParam String otp){
        buyerService.confirmOtpAndRegister(dto, otp);
        return ResponseEntity.ok(new ApiResponse("تم تسجيلك بنجاح!"));
    }



}
