package com.example.finalprojectjavabootcamp.Controller;

import com.example.finalprojectjavabootcamp.Api.ApiResponse;
import com.example.finalprojectjavabootcamp.DTOIN.SellerDTOIn;
import com.example.finalprojectjavabootcamp.Model.User;
import com.example.finalprojectjavabootcamp.Service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;


    @PutMapping("/update")
    public ResponseEntity<?> updateSeller(@AuthenticationPrincipal User user,
                                          @RequestBody SellerDTOIn dto) {
        sellerService.updateSeller(user.getId(), dto);
        return ResponseEntity.status(200).body(new ApiResponse("Seller updated successfully"));
    }

    //EXTRA:
    @PostMapping("/register/request-otp")
    public ResponseEntity<?> requestOtp(@RequestBody SellerDTOIn dto){
        sellerService.requestOtp(dto);
        return ResponseEntity.ok(new ApiResponse("تم إرسال OTP إلى رقمك "));
    }

    @PostMapping("/register/confirm-otp")
    public ResponseEntity<?> confirmOtp(@RequestBody SellerDTOIn dto, @RequestParam String otp){
        sellerService.confirmOtpAndRegister(dto, otp);
        return ResponseEntity.ok(new ApiResponse("تم تسجيلك بنجاح!"));
    }

    @GetMapping("/{sellerId}/listings/filter")
    public ResponseEntity<?> getMyListingsByFilters(
            @PathVariable String sellerId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false, defaultValue = "") String query,
            @RequestParam(required = false, defaultValue = "false") Boolean getOlder
    ) {
        return ResponseEntity.ok(
                sellerService.getMyListingsByFilters(sellerId, status, type, query, getOlder)
        );
    }

    @GetMapping("/negotiations/stats")
    public ResponseEntity<?> getMyNegotiationsStats(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(sellerService.getMyNegotiationsStats(user.getId()));
    }

}
