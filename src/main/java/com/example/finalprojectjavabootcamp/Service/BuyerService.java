package com.example.finalprojectjavabootcamp.Service;

import com.example.finalprojectjavabootcamp.Api.ApiException;
import com.example.finalprojectjavabootcamp.DTOIN.BuyerDTOIn;
import com.example.finalprojectjavabootcamp.DTOOUT.ProductDTOOut;
import com.example.finalprojectjavabootcamp.Model.Buyer;
import com.example.finalprojectjavabootcamp.Model.Listing;
import com.example.finalprojectjavabootcamp.Model.User;
import com.example.finalprojectjavabootcamp.Repository.BuyerRepository;
import com.example.finalprojectjavabootcamp.Repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuyerService {

    private final UserRepository userRepository;
    private final BuyerRepository buyerRepository;
    private final ListingService listingService;
    private final WhatsappService whatsappService;
    private final OtpService otpService;


    public void requestOtp(BuyerDTOIn dto) {
        User oldUser = userRepository.findUserByEmail(dto.getEmail());
        if (oldUser != null) throw new ApiException("Email already exists");
        if (!dto.getPassword().equals(dto.getConfirmPassword())) throw new ApiException("Passwords do not match");

        String otp = otpService.generateOtp(dto.getPhone());
        String message = "رمز التحقق الخاص بك هو: " + otp + "\n⏰ صالح لمدة دقيقة واحدة فقط.";
        whatsappService.sendTextMessage(message, dto.getPhone());
    }


    public void confirmOtpAndRegister(BuyerDTOIn dto, String otp) {
        if (!otpService.verifyOtp(dto.getPhone(), otp)) {
            throw new ApiException("OTP غير صالح أو انتهت صلاحيته");
        }



        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setLocation(dto.getLocation());
        user.setPhone(dto.getPhone());
        String hashedPassword = new BCryptPasswordEncoder().encode(dto.getPassword());
        user.setPassword(hashedPassword);
        user.setConfirmPassword(hashedPassword);
        user.setStatus("ACTIVE");
        user.setRole("BUYER");

        Buyer buyer = new Buyer();
        user.setBuyer(buyer);
        buyer.setUser(user);

        userRepository.save(user);
        buyerRepository.save(buyer);

        whatsappService.sendWelcomeMessage(user.getPhone(), user.getName());
    }

    public void updateBuyer(Integer buyerId, BuyerDTOIn dto) {
        User user = userRepository.findUserById(buyerId);

        if(user == null || user.getBuyer() == null){
            throw new ApiException("Buyer not found");
        }

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setLocation(dto.getLocation());

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new ApiException("Passwords do not match");
        }

        String hashedPassword = new BCryptPasswordEncoder().encode(dto.getPassword());
        user.setPassword(hashedPassword);
        user.setConfirmPassword(hashedPassword);

        userRepository.save(user);
    }

    public List<ProductDTOOut> getListingsByFilters(String type, String query, Boolean getNewer){
        List<Listing> listings = listingService.getListingByStatus("listed");
        if (type != null && !listings.isEmpty()){
            listings.retainAll(listingService.getListingByType(type));
        }
        if (!query.isEmpty()  && !listings.isEmpty()){
            listings.retainAll(listingService.searchListing(query));
        }
        if (listings.isEmpty()){
            throw new ApiException("No listings found under these filters");
        }
        if (getNewer){
            listings.sort(Comparator.comparing(listing -> listing.getCreated_at() != null ? listing.getCreated_at() : LocalDateTime.MIN, Comparator.reverseOrder()));
            return listings.stream()
                    .map(listing -> {
                        ProductDTOOut dto = new ProductDTOOut();
                        dto.setTitle(listing.getTitle());
                        dto.setDescription(listing.getDescription());
                        dto.setType(listing.getType().equals("car") ? "car" : "real_estate");
                        dto.setPrice(listing.getLeast_price());
                        dto.setCity(listing.getCity());
                        return dto;
                    })
                    .collect(Collectors.toList());
        }
        listings.sort(Comparator.comparing(listing -> listing.getCreated_at() != null ? listing.getCreated_at() : LocalDateTime.MIN));

        return listings.stream()
                .map(listing -> {
                    ProductDTOOut dto = new ProductDTOOut();
                    dto.setTitle(listing.getTitle());
                    dto.setDescription(listing.getDescription());
                    dto.setType(listing.getType().equals("car") ? "car" : "real_estate");
                    dto.setPrice(listing.getLeast_price());
                    dto.setCity(listing.getCity());
                    return dto;
                })
                .collect(Collectors.toList());


    }
}
