package com.example.finalprojectjavabootcamp.Controller;

import com.example.finalprojectjavabootcamp.Api.ApiResponse;
import com.example.finalprojectjavabootcamp.DTOIN.SearchCarDTOIn;
import com.example.finalprojectjavabootcamp.DTOIN.SearchRealEstateDTOIn;
import com.example.finalprojectjavabootcamp.Service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @PostMapping("/create-car-search/{buyerId}")
    public ResponseEntity<?> createCarSearch(@RequestBody SearchCarDTOIn searchCarDTOIn, @PathVariable Integer buyerId) {
        searchService.CreateCarSearch(searchCarDTOIn, buyerId);
        return ResponseEntity.status(200).body(new ApiResponse("Car search created successfully"));
    }

    @PostMapping("/create-real-estate-search/{buyerId}")
    public ResponseEntity<?> createRealEstateSearch(@RequestBody SearchRealEstateDTOIn searchRealEstateDTOIn, @PathVariable Integer buyerId) {
        searchService.CreateRealEstateSearch(searchRealEstateDTOIn, buyerId);
        return ResponseEntity.status(200).body(new ApiResponse("Real estate search created successfully"));
    }
}