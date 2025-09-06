package com.example.finalprojectjavabootcamp.Controller;

import com.example.finalprojectjavabootcamp.Api.ApiResponse;
import com.example.finalprojectjavabootcamp.Model.Meeting;
import com.example.finalprojectjavabootcamp.Service.MeetingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/meeting")
public class MeetingController {
    private final MeetingService service;


    @PostMapping("/negotiations/{negotiationId}/meetings")
    public ResponseEntity<?> create(@PathVariable Integer negotiationId,
                                          @Valid @RequestBody Meeting body) {
        service.create(negotiationId, body);
        return ResponseEntity.ok(new ApiResponse("meeting created successfully"));
    }


    @PatchMapping("/negotiations/meetings/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,
                                          @Valid @RequestBody Meeting body) {
        service.update(id, body);
        return ResponseEntity.ok(new ApiResponse("meeting updated successfully"));
    }


    @GetMapping("/negotiations/meetings/{id}")
    public ResponseEntity<?> get(@PathVariable Integer id) {

        return ResponseEntity.ok(service.findById(id));
    }


    @GetMapping("/negotiations/{negotiationId}/meetings")
    public ResponseEntity<?> listAsc(@PathVariable Integer negotiationId) {
        return ResponseEntity.ok(service.listAsc(negotiationId));
    }


    @GetMapping("/negotiations/{negotiationId}/meetings/desc")
    public ResponseEntity<?> listDesc(@PathVariable Integer negotiationId) {
        return ResponseEntity.ok(service.listDesc(negotiationId));
    }

    //  cancel (delete)
    @DeleteMapping("/negotiations/meetings/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
