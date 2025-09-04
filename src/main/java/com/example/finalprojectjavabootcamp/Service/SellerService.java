package com.example.finalprojectjavabootcamp.Service;

import com.example.finalprojectjavabootcamp.Api.ApiException;
import com.example.finalprojectjavabootcamp.DTOIN.SellerDTOIn;
import com.example.finalprojectjavabootcamp.Model.Seller;
import com.example.finalprojectjavabootcamp.Model.User;
import com.example.finalprojectjavabootcamp.Repository.SellerRepository;
import com.example.finalprojectjavabootcamp.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;

    public void registerSeller(SellerDTOIn dto){
        User oldUser = userRepository.findUserByEmail(dto.getEmail());

        if (oldUser != null) {
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

        user.setPassword(dto.getPassword());
        user.setConfirmPassword(dto.getConfirmPassword());

        Seller seller = new Seller();

        seller.setDescription(dto.getDescription());
        seller.setPreferredContact(dto.getPreferredContact());

        user.setRole("SELLER");

        user.setSeller(seller);
        seller.setUser(user);

        userRepository.save(user);
        sellerRepository.save(seller);
    }

    public void updateSeller(Integer sellerId, SellerDTOIn dto){
        Seller seller = sellerRepository.findSellerById(sellerId);
        if(seller == null){
            throw new ApiException("Seller not found");
        }

        User oldUser = seller.getUser();

        oldUser.setName(dto.getName());
        oldUser.setEmail(dto.getEmail());
        oldUser.setPhone(dto.getPhone());


        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new ApiException("Passwords do not match");
        }

        oldUser.setPassword(dto.getPassword());
        oldUser.setConfirmPassword(dto.getConfirmPassword());



        seller.setDescription(dto.getDescription());
        seller.setPreferredContact(dto.getPreferredContact());



        oldUser.setSeller(seller);
        seller.setUser(oldUser);

        userRepository.save(oldUser);

    }
}
