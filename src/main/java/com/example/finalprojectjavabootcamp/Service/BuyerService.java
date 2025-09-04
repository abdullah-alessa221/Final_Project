package com.example.finalprojectjavabootcamp.Service;

import com.example.finalprojectjavabootcamp.Api.ApiException;
import com.example.finalprojectjavabootcamp.DTOIN.BuyerDTOIn;
import com.example.finalprojectjavabootcamp.Model.Buyer;
import com.example.finalprojectjavabootcamp.Model.User;
import com.example.finalprojectjavabootcamp.Repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuyerService {

    private final UserRepository userRepository;

    public void registerBuyer(BuyerDTOIn dto){
       User oldUser = userRepository.findUserByEmail(dto.getEmail());


        if(oldUser != null){
            throw new ApiException("Email already exists");
        }


        User user = new User();

       user.setName(dto.getName());
       user.setEmail(dto.getEmail());
       user.setLocation(dto.getLocation());
       user.setPhone(dto.getPhone());

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new ApiException("Passwords do not match");
        }
//        String hashedPassword = new BCryptPasswordEncoder().encode(dto.getPassword());

        user.setPassword(dto.getPassword());
        user.setConfirmPassword(dto.getConfirmPassword());
        user.setRole("BUYER");

        Buyer buyer = new Buyer();

        user.setBuyer(buyer);
        buyer.setUser(user);

        userRepository.save(user);
    }
}
