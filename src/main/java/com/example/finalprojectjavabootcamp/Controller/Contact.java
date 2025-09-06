package com.example.finalprojectjavabootcamp.Controller;

import com.example.finalprojectjavabootcamp.Api.ApiResponse;
import com.example.finalprojectjavabootcamp.Service.ContactUsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contact-us")
@RequiredArgsConstructor
public class Contact {

    private final ContactUsService contactUsService;

    @PostMapping("/new")
    public ResponseEntity<?> createNew(@Valid @RequestBody com.example.finalprojectjavabootcamp.Model.Contact c){
        contactUsService.createNew(c);
        return ResponseEntity.ok(new ApiResponse("Sent successfully"));

    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(contactUsService.getAll());
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id){
        return ResponseEntity.ok(contactUsService.getById(id));
    }
}
