package com.example.finalprojectjavabootcamp.Controller;


import com.example.finalprojectjavabootcamp.Api.ApiResponse;
import com.example.finalprojectjavabootcamp.DTOIN.CarDTOIn;
import com.example.finalprojectjavabootcamp.Service.CarListingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/car-listing")
public class CarListingController {

    private final CarListingService carListingService;


    @GetMapping("/get/all")
    public ResponseEntity<?> getCarListings(){
        return ResponseEntity.status(HttpStatus.OK.value()).body(carListingService.getAllCarListings());
    }

    @PostMapping("/list/{sellerId}")
    public ResponseEntity<?> listCar(@PathVariable Integer sellerId, @RequestBody @Valid CarDTOIn carDTOIn) {
        carListingService.listCar(carDTOIn, sellerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Car listed successfully"));
    }

}
