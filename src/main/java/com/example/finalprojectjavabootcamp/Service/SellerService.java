package com.example.finalprojectjavabootcamp.Service;

import com.example.finalprojectjavabootcamp.Api.ApiException;
import com.example.finalprojectjavabootcamp.DTOIN.SellerDTOIn;
import com.example.finalprojectjavabootcamp.DTOOut.MyListingStatsDTOOut;
import com.example.finalprojectjavabootcamp.Model.Listing;
import com.example.finalprojectjavabootcamp.Model.Negotiation;
import com.example.finalprojectjavabootcamp.Model.Seller;
import com.example.finalprojectjavabootcamp.Model.User;
import com.example.finalprojectjavabootcamp.Repository.NegotiationRepository;
import com.example.finalprojectjavabootcamp.Repository.SellerRepository;
import com.example.finalprojectjavabootcamp.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;
    private final ListingService listingService;
    private final NegotiationRepository negotiationRepository;

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

    public List<Listing> getMyListingsByFilters(String sellerId, String status, String type, String query, Boolean getOlder){
        Seller seller = sellerRepository.findSellerById(Integer.parseInt(sellerId));
        if (seller == null){
            throw new ApiException("Seller not found");
        }
        List<Listing> listings = listingService.getListingBySellerId(seller.getId());

        if (status != null && !listings.isEmpty()){
            listings.retainAll(listingService.getListingByStatus(status));
        }
        if (type != null && !listings.isEmpty()){
            listings.retainAll(listingService.getListingByType(type));
        }
        if (!query.isEmpty()  && !listings.isEmpty()){
            listings.retainAll(listingService.searchListing(query));
        }
        if (listings.isEmpty()){
            throw new ApiException("No listings found under these filters");
        }
        if (getOlder){
            listings.sort(Comparator.comparing(Listing::getCreated_at).reversed());
            return listings;
        }
        listings.sort(Comparator.comparing(Listing::getCreated_at));
        return listings;
    }


    public MyListingStatsDTOOut getMyNegotiationsStats(Integer id){
        List<Negotiation> negotiations =  negotiationRepository.findNegotiationsBySellerId(id);
        if (negotiations == null){
            throw new ApiException("Listing not found");
        }

        return new MyListingStatsDTOOut( negotiationRepository.findNegotiationsByStatus("waiting_offer").size() * 100.0 /negotiations.size(),negotiationRepository.findNegotiationsByStatus("waiting_acceptance").size() * 100.0 /negotiations.size(),negotiationRepository.findNegotiationsByStatus("accepted").size() * 100.0 /negotiations.size(),negotiationRepository.findNegotiationsByStatus("rejected").size() * 100.0 /negotiations.size());
    }
}
