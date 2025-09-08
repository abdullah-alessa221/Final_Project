package com.example.finalprojectjavabootcamp.Controller;

import com.example.finalprojectjavabootcamp.Api.ApiResponse;
import com.example.finalprojectjavabootcamp.DTOIN.RealEstateDTOIn;
import com.example.finalprojectjavabootcamp.Model.User;
import com.example.finalprojectjavabootcamp.Service.RealEstateListingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/real-estate-listing")
public class RealEstateListingController {

    private final RealEstateListingService realEstateListingService;

    @GetMapping("/get/all")
    public ResponseEntity<?> getRealEstateListings() {
        return ResponseEntity.status(HttpStatus.OK).body(realEstateListingService.getAllRealEstateListings());
    }

    @PostMapping("/list")
    public ResponseEntity<?> listRealEstate(@AuthenticationPrincipal User user, @RequestBody @Valid RealEstateDTOIn realEstateDTOIn) {
        realEstateListingService.listRealEstate(realEstateDTOIn, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Real estate listed successfully"));
    }
}

