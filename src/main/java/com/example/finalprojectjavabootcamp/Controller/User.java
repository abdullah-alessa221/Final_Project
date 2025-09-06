package com.example.finalprojectjavabootcamp.Controller;

import com.example.finalprojectjavabootcamp.Api.ApiResponse;
import com.example.finalprojectjavabootcamp.DTOOUT.SellerDTOOut;
import com.example.finalprojectjavabootcamp.Service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    //EXTRA
    @GetMapping("/get-all-dto")
    public ResponseEntity<?> getAllUsersDTO(){
        return ResponseEntity.ok(userService.getAllUsersDTO());
    }

    @GetMapping("/sellers")
    public ResponseEntity<?> getAllSellersDTO() {
        return ResponseEntity.ok(userService.getAllSellersDTO());
    }

    @GetMapping("/buyers")
    public ResponseEntity<?> getAllBuyerDTO(){
        return ResponseEntity.ok(userService.getAllBuyerDTO());
    }

    @PutMapping("/block-user/{userId}")
    public ResponseEntity<?> blockUser(@PathVariable Integer userId){
        userService.blockUser(userId);
        return ResponseEntity.ok(new ApiResponse("User blocked successfully"));
    }

    @GetMapping("/get-blocked-users")
    public ResponseEntity<?> getBlockedUsers(){
        return ResponseEntity.ok(userService.getBlockedUsers());
    }

    @PutMapping("/active-user/{userId}")
    public ResponseEntity<?> activeUser(@PathVariable Integer userId){
        userService.activeUser(userId);
        return ResponseEntity.ok(new ApiResponse("User activated successfully"));
    }
}
