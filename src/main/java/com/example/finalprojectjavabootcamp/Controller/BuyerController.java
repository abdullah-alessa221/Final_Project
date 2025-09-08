package com.example.finalprojectjavabootcamp.Controller;

import com.example.finalprojectjavabootcamp.Api.ApiResponse;
import com.example.finalprojectjavabootcamp.DTOIN.BuyerDTOIn;
import com.example.finalprojectjavabootcamp.Model.Buyer;
import com.example.finalprojectjavabootcamp.Model.Listing;
import com.example.finalprojectjavabootcamp.Model.User;
import com.example.finalprojectjavabootcamp.Service.BuyerService;
import com.example.finalprojectjavabootcamp.Service.ListingService;
import com.example.finalprojectjavabootcamp.Service.RatingService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/buyer")
@RequiredArgsConstructor
public class BuyerController {

    private final BuyerService buyerService;


    @PutMapping("/update")
    public ResponseEntity<?> updateBuyer(@AuthenticationPrincipal User user, @Valid @RequestBody BuyerDTOIn dto) {
        buyerService.updateBuyer(user.getId(), dto);
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

    @GetMapping("/filter")
    public ResponseEntity<?> filterListings(
            @RequestParam(required = false) String type,
            @RequestParam(required = false, defaultValue = "") String query,
            @RequestParam(required = false, defaultValue = "false") Boolean getOlder
    ) {
        List<Listing> result = buyerService.getListingsByFilters(type, query, getOlder);
        return ResponseEntity.ok(result);
    }



}
