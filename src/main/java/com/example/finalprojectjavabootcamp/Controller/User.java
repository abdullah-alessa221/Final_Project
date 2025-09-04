package com.example.finalprojectjavabootcamp.Controller;

import com.example.finalprojectjavabootcamp.Api.ApiResponse;
import com.example.finalprojectjavabootcamp.Service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class User {

    private final UserService userService;

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllUsers(){
      return   ResponseEntity.status(200).body(userService.getAllUsers());
    }
    @GetMapping("/get-user-by-id/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id){
        return ResponseEntity.status(200).body(userService.getUserById(id));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id){
        userService.deleteUser(id);

        return ResponseEntity.status(200).body(new ApiResponse("User deleted successfully"));
    }
}
